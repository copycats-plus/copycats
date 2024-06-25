package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlockStateProperties.Side;
import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock.FaceShape;
import com.copycatsplus.copycats.content.copycat.stairs.WrappedStairsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.minecraft.core.Direction.*;
import static net.minecraft.world.level.block.StairBlock.HALF;

@SuppressWarnings("deprecation")
public class CopycatVerticalStairBlock extends CTWaterloggedCopycatBlock implements ICustomCTBlocking, IStateType {

    // Facing refers to the direction of the base of the stairs
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Side> SIDE = CCBlockStateProperties.SIDE;
    public static final EnumProperty<VerticalStairShape> SHAPE = CCBlockStateProperties.VERTICAL_STAIR_SHAPE;

    public CopycatVerticalStairBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SIDE, Side.LEFT)
                .setValue(SHAPE, VerticalStairShape.STRAIGHT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, SIDE, SHAPE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        Side side = context.getClickLocation().get(facing.getClockWise().getAxis()) - context.getClickedPos().get(facing.getClockWise().getAxis()) > 0.5
                ? Side.RIGHT : Side.LEFT;
        if (facing.getCounterClockWise().getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            side = side.getOpposite();
        }
        BlockState blockState = defaultBlockState().setValue(FACING, facing).setValue(SIDE, side);
        return withWater(blockState.setValue(SHAPE, getStairsShape(blockState, context.getLevel(), blockPos)), context);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        boolean right = state.getValue(SIDE).isRight();
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT ->
                    right ? CCShapes.VERTICAL_STAIR_STRAIGHT_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_STRAIGHT_LEFT.get(facing);
            case INNER_TOP ->
                    right ? CCShapes.VERTICAL_STAIR_INNER_TOP_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_INNER_TOP_LEFT.get(facing);
            case INNER_BOTTOM ->
                    right ? CCShapes.VERTICAL_STAIR_INNER_BOTTOM_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_INNER_BOTTOM_LEFT.get(facing);
            case OUTER_TOP ->
                    right ? CCShapes.VERTICAL_STAIR_OUTER_TOP_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_TOP_LEFT.get(facing);
            case OUTER_BOTTOM ->
                    right ? CCShapes.VERTICAL_STAIR_OUTER_BOTTOM_RIGHT.get(facing) : CCShapes.VERTICAL_STAIR_OUTER_BOTTOM_LEFT.get(facing);
        };
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockState newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction.getAxis() != newState.getValue(FACING).getAxis()) {
            return newState.setValue(SHAPE, getStairsShape(newState, level, pos));
        } else {
            return newState;
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState pState) {
        return true;
    }

    private static VerticalStairShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean side = state.getValue(SIDE).isRight();

        Direction verticalDirection = null;
        // true for right, false for left, null for vertical connections only
        Boolean horizontalConnection = null;

        boolean down = canConnect(state, level, pos, Direction.DOWN);
        boolean downParity = getVerticalParity(state, level, pos, Direction.DOWN);
        if (down && side == downParity) {
            verticalDirection = Direction.DOWN;
        } else {
            boolean up = canConnect(state, level, pos, Direction.UP);
            boolean upParity = getVerticalParity(state, level, pos, Direction.UP);
            if (up && side == upParity) {
                verticalDirection = Direction.UP;
            }
        }
        boolean left = canConnect(state, level, pos, facing.getCounterClockWise());
        boolean leftParity = getHorizontalParity(state, level, pos, facing.getCounterClockWise());
        if (left && (verticalDirection == null || verticalDirection == Direction.DOWN && side == leftParity || verticalDirection == Direction.UP && side != leftParity)) {
            horizontalConnection = false;
            if (verticalDirection == null) {
                verticalDirection = leftParity == side ? Direction.DOWN : Direction.UP;
            }
        } else {
            boolean right = canConnect(state, level, pos, facing.getClockWise());
            boolean rightParity = getHorizontalParity(state, level, pos, facing.getClockWise());
            if (right && (verticalDirection == null || verticalDirection == Direction.UP && side == rightParity || verticalDirection == Direction.DOWN && side != rightParity)) {
                horizontalConnection = true;
                if (verticalDirection == null) {
                    verticalDirection = rightParity == side ? Direction.UP : Direction.DOWN;
                }
            }
        }

        if (horizontalConnection == null) {
            return VerticalStairShape.STRAIGHT;
        } else if (!horizontalConnection) {
            if (verticalDirection == Direction.DOWN) {
                return side ? VerticalStairShape.INNER_TOP : VerticalStairShape.OUTER_BOTTOM;
            } else {
                return side ? VerticalStairShape.INNER_BOTTOM : VerticalStairShape.OUTER_TOP;
            }
        } else {
            if (verticalDirection == Direction.DOWN) {
                return side ? VerticalStairShape.OUTER_BOTTOM : VerticalStairShape.INNER_TOP;
            } else {
                return side ? VerticalStairShape.OUTER_TOP : VerticalStairShape.INNER_BOTTOM;
            }
        }
    }

    private static boolean getVerticalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!(blockState.getBlock() instanceof CopycatVerticalStairBlock)) return false;
        return blockState.getValue(SIDE).isRight();
    }

    private static boolean getHorizontalParity(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) return false;
        if (blockState.getBlock() instanceof CopycatVerticalStairBlock) {
            return (state.getValue(FACING).getCounterClockWise() == face) == blockState.getValue(SIDE).isRight();
        } else {
            return blockState.getValue(StairBlock.HALF) == Half.TOP;
        }
    }

    private static boolean canConnect(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        if (!isStairs(blockState)) return false;
        if (face.getAxis().isVertical() && !(blockState.getBlock() instanceof CopycatVerticalStairBlock)) return false;
        Direction selfFacing = state.getValue(FACING);
        Direction otherFacing = blockState.getValue(FACING);
        if (selfFacing == otherFacing.getOpposite()) return false;
        if (selfFacing == otherFacing && face.getAxis() != selfFacing.getAxis()) return true;
        return selfFacing != otherFacing;
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof StairBlock || state.getBlock() instanceof CopycatVerticalStairBlock || state.getBlock() instanceof WrappedStairsBlock || state.getBlock() instanceof CopycatStairsBlock;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        Direction facing = state.getValue(FACING);
        Side side = state.getValue(SIDE);
        Axis axis = switch (mirror) {
            case LEFT_RIGHT -> Axis.X;
            case FRONT_BACK -> Axis.Z;
            default -> Axis.Y;
        };
        if (axis == Axis.Y) return state;
        if (facing.getAxis() == axis) {
            return state.setValue(FACING, facing.getOpposite()).setValue(SIDE, side.getOpposite());
        } else {
            return state.setValue(SIDE, side.getOpposite());
        }
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        boolean right = state.getValue(SIDE).isRight();
        Direction facing = state.getValue(FACING);
        BlockState toState = reader.getBlockState(toPos);
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }

        if (isStairs(toState)) {
            return false;
        } else {
            if (diff.get(facing.getAxis()) == 0) {
                // if target is level with this block,
                // only allows it to connect if it's adjacent to a full face of this block
                VerticalStairShape shape = state.getValue(SHAPE);
                int fullCount = 0;
                if (diff.getX() != 0) {
                    FaceShape faceShape = getFaceShape(state, fromAxisAndDirection(Axis.X, directionOf(diff.getX())));
                    if (faceShape.isFull())
                        fullCount++;
                    else if ((shape == VerticalStairShape.OUTER_BOTTOM || shape == VerticalStairShape.OUTER_TOP) && (diff.getY() != 0 || diff.getZ() != 0)) {
                        if (diff.getX() > 0 && faceShape.topNegative && faceShape.bottomNegative || diff.getX() < 0 && faceShape.topPositive && faceShape.bottomPositive)
                            fullCount++;
                    }
                }
                if (diff.getY() != 0) {
                    FaceShape faceShape = getFaceShape(state, fromAxisAndDirection(Axis.Y, directionOf(diff.getY())));
                    if (faceShape.isFull())
                        fullCount++;
                    else if ((shape == VerticalStairShape.OUTER_BOTTOM || shape == VerticalStairShape.OUTER_TOP) && (diff.getX() != 0 || diff.getZ() != 0)) {
                        if (diff.getY() > 0 && faceShape.topNegative && faceShape.topPositive || diff.getX() < 0 && faceShape.bottomNegative && faceShape.bottomPositive)
                            fullCount++;
                    }
                }
                if (diff.getZ() != 0) {
                    FaceShape faceShape = getFaceShape(state, fromAxisAndDirection(Axis.Z, directionOf(diff.getZ())));
                    if (faceShape.isFull())
                        fullCount++;
                    else if ((shape == VerticalStairShape.OUTER_BOTTOM || shape == VerticalStairShape.OUTER_TOP) && (diff.getX() != 0 || diff.getY() != 0)) {
                        if (diff.getZ() > 0 && faceShape.topNegative && faceShape.bottomNegative || diff.getZ() < 0 && faceShape.topPositive && faceShape.bottomPositive)
                            fullCount++;
                    }
                }
                return fullCount < Mth.abs(diff.getX()) + Mth.abs(diff.getY()) + Mth.abs(diff.getZ());
            } else {
                // if target is not level with this block,
                // only allow connections below the base of this block
                return diff.get(facing.getAxis()) * (facing.getAxisDirection() == AxisDirection.POSITIVE ? -1 : 1) > 0;
            }
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction side = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());

        if (side != null) {
            FaceShape sideShape = getFaceShape(state, side);
            if (!sideShape.canConnect()) return false;
            if (isStairs(toState)) {
                if (!sideShape.equals(getFaceShape(toState, side.getOpposite()))) return false;
            } else {
                if (!sideShape.isFull()) return false;
            }
        }

        return true;
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        if (!getFaceShape(state, face).canConnect())
            return Optional.of(false);
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        FaceShape sideShape = getFaceShape(state, face);
        Direction facing = state.getValue(FACING);
        if (!sideShape.canConnect()) return Optional.of(false);
        BlockState connectingState = reader.getBlockState(connectingPos);
        if (connectingState.is(this)) {
            if (sideShape.equals(getFaceShape(connectingState, face.getOpposite())))
                return Optional.of(true);
        } else if (sideShape.isFull()) {
            BlockState ctState = reader.getBlockState(ctPos);
            if (ctPos.get(facing.getAxis()) == pos.get(facing.getAxis()) ||
                    !isStairs(ctState) ||
                    ctState.getOptionalValue(SIDE).orElse(null) != state.getOptionalValue(SIDE).orElse(null) ||
                    ctState.getOptionalValue(HALF).orElse(null) != state.getOptionalValue(HALF).orElse(null))
                return Optional.of(true);
        }
        return Optional.empty();
    }

    private static AxisDirection directionOf(int value) {
        return value >= 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        int count = getFaceShape(state, face).countBlocks();
        return count == 4 || count == 3 && state.getValue(SHAPE) == VerticalStairShape.STRAIGHT;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (neighborState.getBlock() instanceof CopycatVerticalStairBlock) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()))
                return getFaceShape(state, dir).equals(getFaceShape(neighborState, dir.getOpposite()));
        }

        return getFaceShape(state, dir).isFull()
                && getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }

    /**
     * Return the area of the face that is at the edge of the block.
     */
    public static FaceShape getFaceShape(BlockState state, Direction face) {
        if (!(state.getBlock() instanceof CopycatVerticalStairBlock)) {
            return CopycatStairsBlock.getFaceShape(state, face);
        }
        boolean right = state.getValue(SIDE).isRight();
        Direction facing = state.getValue(FACING);
        VerticalStairShape shape = state.getValue(SHAPE);
        if (face == facing) return new FaceShape().fillAll();

        FaceShape faceShape = new FaceShape();

        switch (shape) {
            case STRAIGHT -> {
                if (face == facing.getOpposite())
                    return right ? faceShape.fillRight(facing) : faceShape.fillLeft(facing);
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing);
                }
                faceShape.fillTop();
                if (right)
                    faceShape.fillPositive();
                else
                    faceShape.fillNegative();
                return faceShape.rotate(facing.toYRot());
            }
            case INNER_BOTTOM -> {
                if (face == facing.getOpposite()) {
                    faceShape.fillBottom();
                    if (right)
                        faceShape.fillRight(facing);
                    else
                        faceShape.fillLeft(facing);
                    return faceShape;
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillBottom();
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillBottom();
                }
                if (face == Direction.DOWN)
                    return faceShape.fillAll();
                faceShape.fillTop();
                if (right)
                    faceShape.fillPositive();
                else
                    faceShape.fillNegative();
                return faceShape.rotate(facing.toYRot());
            }
            case INNER_TOP -> {
                if (face == facing.getOpposite()) {
                    faceShape.fillTop();
                    if (right)
                        faceShape.fillRight(facing);
                    else
                        faceShape.fillLeft(facing);
                    return faceShape;
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillAll() : faceShape.fillLeft(facing).fillTop();
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillAll() : faceShape.fillRight(facing).fillTop();
                }
                if (face == Direction.UP)
                    return faceShape.fillAll();
                faceShape.fillTop();
                if (right)
                    faceShape.fillPositive();
                else
                    faceShape.fillNegative();
                return faceShape.rotate(facing.toYRot());
            }
            case OUTER_BOTTOM -> {
                if (face == facing.getOpposite()) {
                    return faceShape.fillBottom(right
                            ? facing.getCounterClockWise().getAxisDirection()
                            : facing.getClockWise().getAxisDirection()
                    );
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillLeft(facing).fillBottom() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillRight(facing).fillBottom() : faceShape.fillRight(facing);
                }
                if (face == Direction.UP)
                    return faceShape.fillTop().rotate(facing.toYRot());
                faceShape.fillTop();
                if (right)
                    faceShape.fillPositive();
                else
                    faceShape.fillNegative();
                return faceShape.rotate(facing.toYRot());
            }
            case OUTER_TOP -> {
                if (face == facing.getOpposite()) {
                    return faceShape.fillTop(right
                            ? facing.getCounterClockWise().getAxisDirection()
                            : facing.getClockWise().getAxisDirection()
                    );
                }
                if (face == facing.getCounterClockWise()) {
                    return !right ? faceShape.fillLeft(facing).fillTop() : faceShape.fillLeft(facing);
                }
                if (face == facing.getClockWise()) {
                    return right ? faceShape.fillRight(facing).fillTop() : faceShape.fillRight(facing);
                }
                if (face == Direction.DOWN)
                    return faceShape.fillTop().rotate(facing.toYRot());
                faceShape.fillTop();
                if (right)
                    faceShape.fillPositive();
                else
                    faceShape.fillNegative();
                return faceShape.rotate(facing.toYRot());
            }
        }
        return faceShape;
    }
}
