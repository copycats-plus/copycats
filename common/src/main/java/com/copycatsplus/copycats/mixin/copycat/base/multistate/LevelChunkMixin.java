package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
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

/**
 * Migrate copycat block entities to multi-state copycat block entities when loading worlds.
 */
@Mixin(value = LevelChunk.class)
public abstract class LevelChunkMixin {

    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);

    @Inject(method = "setBlockEntity", at = @At(value = "HEAD"), cancellable = true)
    private void migrateBlockEntity(BlockEntity blockEntity, CallbackInfo ci) {
        BlockPos pos = blockEntity.getBlockPos();
        LevelChunk chunk = (LevelChunk) (Object) this;

        BlockState state = getBlockState(blockEntity.getBlockPos());
        if (isCopycatAndNeedingConversion(state, blockEntity)) {
            if (CCBlocks.getAllRegisteredMultiStateBlocks().stream().map(RegistryEntry::get).collect(Collectors.toSet()).contains(state.getBlock())) {
                if (CCConfigs.safeGetter(() -> !CCConfigs.common().disableConversion.get(), true).get()) {
                    CompoundTag oldTag = blockEntity.saveWithFullMetadata();

                    // Create and initialize the new BlockEntity
                    MultiStateCopycatBlockEntity newBlockEntity = CCBlockEntityTypes.MULTI_STATE_COPYCAT.create(pos, state);
                    newBlockEntity.load(oldTag);

                    // Migrate data from the old BlockEntity
                    newBlockEntity.migrateData((ICopycatBlockEntity) blockEntity);

                    // Replace the old BlockEntity with the new one in the chunk
                    chunk.removeBlockEntity(pos);
                    chunk.setBlockEntity(newBlockEntity);

                    ci.cancel();
                }
            } else {
                CompoundTag oldTag = blockEntity.saveWithFullMetadata();

                CCCopycatBlockEntity newBlockEntity = CCBlockEntityTypes.COPYCAT.create(pos, state);
                newBlockEntity.load(oldTag);

                chunk.removeBlockEntity(pos);
                chunk.setBlockEntity(newBlockEntity);

                ci.cancel();
            }
        }
    }


    @Unique
    private boolean isCopycatAndNeedingConversion(BlockState state, BlockEntity blockEntity) {
        ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType());
        ResourceKey<Block> resourceKey = state.getBlock().builtInRegistryHolder().key();
        if (id.toString().equalsIgnoreCase("create:copycat")) {
            if (resourceKey.location().getNamespace().equalsIgnoreCase(Copycats.MODID)) {
                return true;
            }
        }
        return false;
    }
}
