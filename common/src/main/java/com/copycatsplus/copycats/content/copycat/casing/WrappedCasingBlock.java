package com.copycatsplus.copycats.content.copycat.casing;

import com.copycatsplus.copycats.utility.BlockUtils;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WrappedCasingBlock extends CasingBlock {
    public WrappedCasingBlock(Properties properties) {
        super(properties);
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean skipRendering(@NotNull BlockState state, BlockState adjacentState, @NotNull Direction direction) {
        return unwrapBlock(state.getBlock()) == unwrapBlock(adjacentState.getBlock());
    }

    public static Block unwrapBlock(Block instance) {
        Block unwrapped = CopycatCasingBlock.REVERSE_ACCEPTED_CASINGS.get().get(instance);
        if (unwrapped != null) {
            return unwrapped;
        }
        return instance;
    }

    public static BlockState unwrapBlock(BlockState state) {
        Block unwrapped = CopycatCasingBlock.REVERSE_ACCEPTED_CASINGS.get().get(state.getBlock());
        if (unwrapped != null) {
            return BlockUtils.tryCopyProperties(state, unwrapped.defaultBlockState());
        }
        return state;
    }
}