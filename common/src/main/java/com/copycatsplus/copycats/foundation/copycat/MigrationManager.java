package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.AllBlockEntityTypes;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.stream.Collectors;

public class MigrationManager {
    public static boolean migrationDisabled() {
        return CCConfigs.safeGetter(() -> CCConfigs.common().disableMigration.get(), false).get();
    }

    public static StructureBlockInfo migrateStructure(StructureBlockInfo info) {
        if (migrationDisabled()) return info;

        BlockState state = info.state();
        CompoundTag nbt = info.nbt();
        if (state.getBlock() instanceof MultiStateCopycatBlock && nbt != null && nbt.contains("Material")) {
            BlockPos pos = info.pos();
            CopycatBlockEntity be = AllBlockEntityTypes.COPYCAT.create(pos, state);
            be.load(nbt);
            MultiStateCopycatBlockEntity multiBe = CCBlockEntityTypes.MULTI_STATE_COPYCAT.create(pos, state);
            multiBe.migrateData((ICopycatBlockEntity) be);
            nbt = multiBe.saveWithId();
            return new StructureTemplate.StructureBlockInfo(pos, state, nbt);
        } else if (state.getBlock() instanceof CCCopycatBlock &&
                nbt != null &&
                nbt.contains("id") &&
                nbt.getString("id").equals(AllBlockEntityTypes.COPYCAT.getId().toString())) {
            BlockPos pos = info.pos();
            CCCopycatBlockEntity be = CCBlockEntityTypes.COPYCAT.create(pos, state);
            be.load(nbt);
            nbt = be.saveWithId();
            return new StructureTemplate.StructureBlockInfo(pos, state, nbt);
        }
        return info;
    }

    public static BlockEntity migrateBlockEntity(LevelChunk chunk, BlockEntity blockEntity) {
        if (migrationDisabled()) return blockEntity;

        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = chunk.getBlockState(pos);
        if (isCopycatAndNeedingConversion(state, blockEntity)) {
            if (CCBlocks.getAllRegisteredMultiStateBlocks().stream().map(RegistryEntry::get).collect(Collectors.toSet()).contains(state.getBlock())) {
                CompoundTag oldTag = blockEntity.saveWithFullMetadata();

                // Create and initialize the new BlockEntity
                MultiStateCopycatBlockEntity newBlockEntity = CCBlockEntityTypes.MULTI_STATE_COPYCAT.create(pos, state);
                newBlockEntity.load(oldTag);

                // Migrate data from the old BlockEntity
                newBlockEntity.migrateData((ICopycatBlockEntity) blockEntity);

                return newBlockEntity;
            } else {
                CompoundTag oldTag = blockEntity.saveWithFullMetadata();

                CCCopycatBlockEntity newBlockEntity = CCBlockEntityTypes.COPYCAT.create(pos, state);
                newBlockEntity.load(oldTag);

                return newBlockEntity;
            }
        }
        return blockEntity;
    }

    private static boolean isCopycatAndNeedingConversion(BlockState state, BlockEntity blockEntity) {
        ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntity.getType());
        if (id == null) return false;
        ResourceKey<Block> resourceKey = state.getBlock().builtInRegistryHolder().key();
        if (id.toString().equalsIgnoreCase("create:copycat")) {
            if (resourceKey.location().getNamespace().equalsIgnoreCase(Copycats.MODID)) {
                return true;
            }
        }
        return false;
    }
}
