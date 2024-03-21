package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.copycatsplus.copycats.content.copycat.base.WaterloggedCopycatWrappedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.CrossCollisionBlock.*;

public class CopycatPaneBlock extends WaterloggedCopycatWrappedBlock<WrappedPaneBlock> implements ICustomCTBlocking {

    public static WrappedPaneBlock pane;
    public CopycatPaneBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Override
    public WrappedPaneBlock getWrappedBlock() {
        return pane;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(NORTH, EAST, SOUTH, WEST));
    }

    @Override
    public BlockState copyState(BlockState from, BlockState to, boolean includeWaterlogged) {
        return to
                .setValue(NORTH, from.getValue(NORTH))
                .setValue(SOUTH, from.getValue(SOUTH))
                .setValue(EAST, from.getValue(EAST))
                .setValue(WEST, from.getValue(WEST))
                .setValue(WATERLOGGED, includeWaterlogged ? from.getValue(WATERLOGGED) : to.getValue(WATERLOGGED));
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is(this);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return pane.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pNeighborPos) {
        return pane.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        return pane.rotate(pState, pRotation);
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return pane.mirror(pState, pMirror);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pane.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            return (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()));
        }

        return getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public static BooleanProperty propertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> throw new IllegalStateException("Direction must be horizontal");
        };
    }
}
