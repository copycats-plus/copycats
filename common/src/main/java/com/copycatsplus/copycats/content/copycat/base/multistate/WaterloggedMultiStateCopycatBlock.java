package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

public abstract class WaterloggedMultiStateCopycatBlock extends MultiStateCopycatBlock implements ProperWaterloggedBlock {
    public WaterloggedMultiStateCopycatBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(WATERLOGGED));
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return fluidState(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return withWater(super.getStateForPlacement(pContext), pContext);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState,
                                           @NotNull Direction pDirection,
                                           @NotNull BlockState pNeighborState,
                                           @NotNull LevelAccessor pLevel,
                                           @NotNull BlockPos pCurrentPos,
                                           @NotNull BlockPos pNeighborPos) {
        super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
        updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }
}
