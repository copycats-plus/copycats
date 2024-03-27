package com.copycatsplus.copycats.content.copycat.block;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopycatBlockBlock extends CTCopycatBlock implements ICopycatWithWrappedBlock<Block> {

    public CopycatBlockBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Block getWrappedBlock() {
        return Blocks.STONE;
    }

    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
                if (!toState.is(this)) return false;
        if (!state.is(this)) return false;
        return true;
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return (canConnectTexturesToward(reader, fromPos, toPos, otherState) ? getMaterial(reader, toPos) : null);
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Shapes.block();
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            return (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()));
        }

        return getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return super.isPathfindable(pState, pLevel, pPos, pType);
    }
}
