package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.CCBlockStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * Placeholder block for copycats without an assigned material.
 */
public class CopycatBaseBlock extends Block {

    public static final int BASE_TYPE_COUNT = 3;
    public static final IntegerProperty BASE_TYPE = CCBlockStateProperties.BASE_TYPE;

    public CopycatBaseBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BASE_TYPE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(BASE_TYPE));
    }
}
