package com.copycatsplus.copycats.content.copycat.stairs;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class WrappedStairsBlock extends StairBlock {
    public WrappedStairsBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }
}
