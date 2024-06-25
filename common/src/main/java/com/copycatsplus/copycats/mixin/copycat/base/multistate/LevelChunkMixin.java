package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(value = LevelChunk.class)
public abstract class LevelChunkMixin {


    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);

    @Inject(method = "setBlockEntity", at = @At(value = "HEAD"), cancellable = true)
    private void idfkanymore(BlockEntity blockEntity, CallbackInfo ci) {
        BlockPos pos = blockEntity.getBlockPos();
        LevelChunk chunk = (LevelChunk) (Object) this;

        if (isMultiStateAndNeedingConversion(blockEntity)) {
            if (CCConfigs.safeGetter(() -> !CCConfigs.common().disableConversion.get(), true).get()) {
                CompoundTag oldTag = blockEntity.saveWithFullMetadata();

                // Create and initialize the new BlockEntity
                MultiStateCopycatBlockEntity newBlockEntity = CCBlockEntityTypes.MULTI_STATE_COPYCAT_BLOCK_ENTITY.create(pos, getBlockState(pos));
                newBlockEntity.load(oldTag);

                // Migrate data from the old BlockEntity
                newBlockEntity.migrateData((CopycatBlockEntity) blockEntity);

                // Replace the old BlockEntity with the new one in the chunk
                chunk.removeBlockEntity(pos);
                chunk.setBlockEntity(newBlockEntity);

                ci.cancel();
            }
        }
    }


    @Unique
    private boolean isMultiStateAndNeedingConversion(BlockEntity blockEntity) {
        ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType());
        BlockState state = getBlockState(blockEntity.getBlockPos());
        ResourceKey<Block> resourceKey = state.getBlock().builtInRegistryHolder().key();
        if (id.toString().equalsIgnoreCase("create:copycat")) {
            if (resourceKey.location().getNamespace().equalsIgnoreCase(Copycats.MODID)) {
                if (CCBlocks.getAllRegisteredMultiStateBlocks().stream().map(RegistryEntry::get).collect(Collectors.toSet()).contains(state.getBlock())) {
                    return true;
                }
            }
        }
        return false;
    }
}
