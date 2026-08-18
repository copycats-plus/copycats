package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.mixin.foundation.copycat.BlockEntityAccessor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockEntityUtils {
    /**
     * Invalidate render data caches for the block entity and re-render it.
     */
    public static void redraw(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            if (level.isClientSide()) {
                requestModelDataUpdate(blockEntity);
            } else {
                blockEntity.setChanged();
            }
            BlockState state = blockEntity.getBlockState();
            level.sendBlockUpdated(blockEntity.getBlockPos(), state, state, 16);
            updateLight(blockEntity);
        }
    }

    public static void saveMetadata(BlockEntity blockEntity, CompoundTag tag) {
        ((BlockEntityAccessor) blockEntity).callSaveMetadata(tag);
    }

    @ExpectPlatform
    public static void requestModelDataUpdate(BlockEntity blockEntity) {
    }

    @ExpectPlatform
    public static void updateLight(BlockEntity blockEntity) {
    }

    public static int getLightEmission(BlockEntity blockEntity) {
        if (blockEntity instanceof IMultiStateCopycatBlockEntity multiStateBE) {
            int lightLevelFromItem = multiStateBE.getLightLevel();
            int light = Math.max(lightLevelFromItem, 0);
            for (BlockState material : multiStateBE.getMaterialItemStorage().getAllMaterials()) {
                light = Math.max(light, material.getLightEmission(multiStateBE.getLevel(), multiStateBE.getBlockPos()));
            }
            return light;
        } else if (blockEntity instanceof ICopycatBlockEntity copycatBE) {
            if (copycatBE.getBlock() instanceof RedstoneLampBlock redstoneLampBlock && blockEntity.getLevel() != null) {
                return redstoneLampBlock.getLightEmission(copycatBE.getBlockState(), blockEntity.getLevel(), blockEntity.getBlockPos());
            }

            int lightLevel = copycatBE.getLightLevel();
            if(lightLevel != -1) return lightLevel;

            return copycatBE.getMaterial().getLightEmission(copycatBE.getLevel(), copycatBE.getBlockPos());
        }
        return 0;
    }
}
