package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.content.copycat.WaterloggedCopycatWrappedBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static net.minecraft.core.Direction.Axis;
import static net.minecraft.world.level.block.WallBlock.*;

@SuppressWarnings("deprecation")
public class CopycatWallBlock extends WaterloggedCopycatWrappedBlock {

    public static WallBlock wall;

    public CopycatWallBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(UP, true)
                .setValue(NORTH_WALL, WallSide.NONE)
                .setValue(SOUTH_WALL, WallSide.NONE)
                .setValue(EAST_WALL, WallSide.NONE)
                .setValue(WEST_WALL, WallSide.NONE)
        );
    }

    @Override
    public Block getWrappedBlock() {
        return wall;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(UP, NORTH_WALL, SOUTH_WALL, EAST_WALL, WEST_WALL));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState state = wall.getStateForPlacement(pContext);
        if (state == null) return super.getStateForPlacement(pContext);
        return copyState(state, super.getStateForPlacement(pContext), false);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return wall.getShape(copyState(pState, wall.defaultBlockState(), true), pLevel, pPos, pContext);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return wall.getCollisionShape(copyState(pState, wall.defaultBlockState(), true), pLevel, pPos, pContext);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return wall.isPathfindable(pState, pLevel, pPos, pType);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return wall.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return wall.propagatesSkylightDown(pState, pLevel, pPos);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        return wall.rotate(pState, pRotation);
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return wall.mirror(pState, pMirror);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this) || !state.is(this)) return true;

        boolean isCross = true;
        for (Direction direction : Iterate.horizontalDirections) {
            if (toState.getValue(byDirection(direction)) == WallSide.NONE) {
                isCross = false;
                break;
            }
        }
        return isCross;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;

        long sideCount = Arrays.stream(Iterate.horizontalDirections).filter(s -> state.getValue(byDirection(s)) != WallSide.NONE).count();
        if (sideCount > 2)
            return false;
        if (sideCount == 2 && (state.getValue(NORTH_WALL) != state.getValue(SOUTH_WALL) || state.getValue(EAST_WALL) != state.getValue(WEST_WALL))) {
            return false;
        }

        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            if (diff.distManhattan(Vec3i.ZERO) > 2) return false;
            if (diff.getY() == 0) return false;
            Direction horizontalDiff = Direction.fromAxisAndDirection(diff.getX() == 0 ? Axis.Z : Axis.X,
                    (diff.getX() + diff.getZ() > 0) ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
            if (diff.getY() > 0) {
                if (state.getValue(byDirection(horizontalDiff)) != WallSide.TALL) return false;
                if (toState.getValue(byDirection(horizontalDiff.getOpposite())) == WallSide.NONE) return false;
            } else {
                if (state.getValue(byDirection(horizontalDiff)) == WallSide.NONE) return false;
                if (toState.getValue(byDirection(horizontalDiff.getOpposite())) != WallSide.TALL) return false;
            }
            return true;
        } else if (face == Direction.DOWN || face == Direction.UP) {
            return canConnectVertically(state) && canConnectVertically(toState);
        } else {
            if (state.getValue(WallBlock.UP)) return false;
            if (state.getValue(byDirection(face)) == WallSide.NONE) return false;
            return true;
        }
    }

    private boolean canConnectVertically(BlockState state) {
        if (!state.getValue(WallBlock.UP)) return false;
        for (Direction direction : Iterate.horizontalDirections) {
            WallSide side = state.getValue(byDirection(direction));
            if (side != WallSide.NONE) return false;
        }
        return true;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        if (face.getAxis().isHorizontal()) {
            WallSide side = state.getValue(byDirection(face));
            return side != WallSide.NONE &&
                    !state.getValue(UP) &&
                    side == state.getValue(byDirection(face.getOpposite())) &&
                    state.getValue(byDirection(face.getClockWise())) == WallSide.NONE &&
                    state.getValue(byDirection(face.getCounterClockWise())) == WallSide.NONE;
        }
        return false;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (neighborState.getBlock() instanceof WallBlock || neighborState.getBlock() instanceof CopycatWallBlock) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                if (dir.getAxis().isHorizontal()) {
                    WallSide side = state.getValue(byDirection(dir));
                    return side != WallSide.NONE && side == neighborState.getValue(byDirection(dir.getOpposite()));
                } else {
                    if (neighborState.getValue(UP) && !state.getValue(UP)) return false;
                    return Arrays.stream(Iterate.horizontalDirections).allMatch(s -> {
                        WallSide neighbor = neighborState.getValue(byDirection(s));
                        WallSide self = state.getValue(byDirection(s));
                        if (dir == Direction.UP && self == WallSide.LOW) return false;
                        if (dir == Direction.DOWN && neighbor == WallSide.LOW) return false;
                        return self == neighbor;
                    });
                }
            }
        }

        return false;
    }

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        BlockState state = CopycatBlock.getMaterial(reader, targetPos);
        if (state.is(Blocks.AIR)) return reader.getBlockState(targetPos);
        return state;
    }

    public static EnumProperty<WallSide> byDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH_WALL;
            case SOUTH -> SOUTH_WALL;
            case WEST -> WEST_WALL;
            case EAST -> EAST_WALL;
            default -> throw new IllegalArgumentException("Vertical directions not supported");
        };
    }

    public static BlockState copyState(BlockState from, BlockState to, boolean includeWaterlogged) {
        return to
                .setValue(UP, from.getValue(UP))
                .setValue(NORTH_WALL, from.getValue(NORTH_WALL))
                .setValue(SOUTH_WALL, from.getValue(SOUTH_WALL))
                .setValue(EAST_WALL, from.getValue(EAST_WALL))
                .setValue(WEST_WALL, from.getValue(WEST_WALL))
                .setValue(WATERLOGGED, includeWaterlogged ? from.getValue(WATERLOGGED) : to.getValue(WATERLOGGED));
    }
}

