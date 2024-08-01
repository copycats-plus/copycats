package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.mixin.foundation.copycat.ChunkAccessAccessor;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.core.jmx.Server;

import javax.annotation.Nullable;
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

    @ExpectPlatform
    public static void requestModelDataUpdate(BlockEntity blockEntity) {

    }

    /**
     * Get the block entity at the target position while not executing in the main thread.
     * <p>
     * Accessing block entities from other threads is unsafe. Use with caution.
     */
    @Nullable
    public static BlockEntity getBlockEntityCrossThread(BlockGetter reader, BlockPos targetPos) {
        try {
            if (reader instanceof Level level) {
                ChunkAccessAccessor chunkAccess = (ChunkAccessAccessor) level.getChunk(targetPos);
                BlockEntity be = chunkAccess.getBlockEntities().get(targetPos);
                if (be.isRemoved()) return null;
                return be;
            }
            return null;
        } catch (Exception $) {
            return null;
        }
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
