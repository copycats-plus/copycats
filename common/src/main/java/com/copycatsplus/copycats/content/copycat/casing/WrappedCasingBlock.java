package com.copycatsplus.copycats.content.copycat.casing;

import com.simibubi.create.content.decoration.encasing.CasingBlock;
import net.minecraft.core.Direction;
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
        return adjacentState.is(this) && state.is(this);
    }
}