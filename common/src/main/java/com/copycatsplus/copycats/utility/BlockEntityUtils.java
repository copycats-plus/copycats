package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.MethodsReturnNonnullByDefault;
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
        requestModelDataUpdate(blockEntity);
        if (blockEntity.getLevel() != null) {
            BlockState state = blockEntity.getBlockState();
            blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), state, state, 16);
            blockEntity.getLevel().getChunkSource()
                    .getLightEngine()
                    .checkBlock(blockEntity.getBlockPos());
        }
    }

    @ExpectPlatform
    public static void requestModelDataUpdate(BlockEntity blockEntity) {

    }
}
