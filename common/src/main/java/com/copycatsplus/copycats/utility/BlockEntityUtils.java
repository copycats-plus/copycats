package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.mixin.foundation.copycat.BlockEntityAccessor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
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
            if (blockEntity instanceof IMultiStateCopycatBlockEntity multiStateBE) {
                CopycatMaterialStore.setMaterial(level, blockEntity.getBlockPos(), multiStateBE.getMaterialItemStorage().getMaterialMap());
            } else if (blockEntity instanceof ICopycatBlockEntity copycatBE) {
                CopycatMaterialStore.setMaterial(level, blockEntity.getBlockPos(), copycatBE.getMaterial());
            }
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

    private static void updateLight(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockPos pos = blockEntity.getBlockPos();
            ProfilerFiller profilerFiller = level.getProfiler();
            profilerFiller.push("updateSkyLightSources");
            level.getChunk(pos).getSkyLightSources().update(level, pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
            profilerFiller.popPush("queueCheckLight");
            level.getChunkSource().getLightEngine().checkBlock(pos);
            profilerFiller.pop();
            level.getChunk(pos).setUnsaved(true);
        }
    }
}
