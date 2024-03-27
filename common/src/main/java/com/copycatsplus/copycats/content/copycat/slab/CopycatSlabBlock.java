package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static com.copycatsplus.copycats.content.copycat.MathHelper.DirectionFromDelta;
import static com.simibubi.create.foundation.block.ProperWaterloggedBlock.WATERLOGGED;

public class CopycatSlabBlock extends CTWaterloggedCopycatBlock implements ICopycatWithWrappedBlock<Block> {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SlabType> SLAB_TYPE = BlockStateProperties.SLAB_TYPE;

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatSlabBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Axis.Y)
                .setValue(SLAB_TYPE, SlabType.BOTTOM));
    }

    @Override
    public Block getWrappedBlock() {
        return Blocks.SMOOTH_STONE_SLAB;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {

        if (!player.isShiftKeyDown() && player.mayBuild()) {
            ItemStack heldItem = player.getItemInHand(hand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray)
                        .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
                return InteractionResult.SUCCESS;
            }
        }

        return super.use(state, world, pos, player, hand, ray);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        Axis axis = state.getValue(AXIS);
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            // connecting to another copycat slab
            if (toState.getValue(AXIS) != axis) return true;
            return getFaceShape(state, face) != getFaceShape(toState, face);
        } else {
            // do not connect slab sides
            if (face.getAxis() != axis) return true;
            // connecting to another block
            return getFaceShape(state, face) != FaceShape.FULL;
        }
    }

    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;
        Axis axis = state.getValue(AXIS);

        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = DirectionFromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            boolean correctAxis = switch (axis) {
                case X -> diff.getX() == 0;
                case Y -> diff.getY() == 0;
                case Z -> diff.getZ() == 0;
            };
            return correctAxis && diff.distManhattan(Vec3i.ZERO) <= 2;
        }

        if (face.getAxis() == axis) return false;

        if (toState.is(this)) {
            return FaceShape.canConnect(getFaceShape(state, face), getFaceShape(toState, face.getOpposite()));
        } else {
            return true;
        }
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return (canConnectTexturesToward(reader, fromPos, toPos, reader.getBlockState(fromPos)) ? getMaterial(reader, toPos) : null);
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return getFaceShape(state, face).hasContact();
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !getFaceShape(state, face).hasContact();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            return state
                    .setValue(SLAB_TYPE, SlabType.DOUBLE)
                    .setValue(WATERLOGGED, false);
        } else {
            Axis axis = context.getNearestLookingDirection().getAxis();
            boolean flag = switch (axis) {
                case X -> context.getClickLocation().x - (double) blockPos.getX() > 0.5D;
                case Y -> context.getClickLocation().y - (double) blockPos.getY() > 0.5D;
                case Z -> context.getClickLocation().z - (double) blockPos.getZ() > 0.5D;
            };
            Direction clickedFace = context.getClickedFace();
            return stateForPlacement
                    .setValue(AXIS, axis)
                    .setValue(SLAB_TYPE, clickedFace == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || clickedFace.getAxis() != axis && !flag ? SlabType.BOTTOM : SlabType.TOP);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (slabtype != SlabType.DOUBLE && itemstack.is(this.asItem())) {
            boolean flag = switch (axis) {
                case X -> pUseContext.getClickLocation().x - (double) pUseContext.getClickedPos().getX() > 0.5D;
                case Y -> pUseContext.getClickLocation().y - (double) pUseContext.getClickedPos().getY() > 0.5D;
                case Z -> pUseContext.getClickLocation().z - (double) pUseContext.getClickedPos().getZ() > 0.5D;
            };
            Direction direction = pUseContext.getClickedFace();
            if (slabtype == SlabType.BOTTOM) {
                return direction == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || flag;
            } else {
                return direction == Direction.fromAxisAndDirection(axis, AxisDirection.NEGATIVE) || !flag;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS).add(SLAB_TYPE));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return super.isPathfindable(pState, pLevel, pPos, pType);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        SlabType type = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (type == SlabType.DOUBLE) {
            return Shapes.block();
        } else if (type == SlabType.BOTTOM) {
            return CCShapes.CASING_8PX.get(axis);
        } else {
            return CCShapes.CASING_8PX_TOP.get(axis);
        }
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (neighborState.getBlock() instanceof SlabBlock || neighborState.getBlock() instanceof CopycatSlabBlock) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()))
                return getFaceShape(state, dir) == getFaceShape(neighborState, dir.getOpposite());
        }

        return getFaceShape(state, dir) == FaceShape.FULL
                && getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        BlockState state = CopycatBlock.getMaterial(reader, targetPos);
        if (state.is(Blocks.AIR)) return reader.getBlockState(targetPos);
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return setApparentDirection(state, rot.rotate(getApparentDirection(state)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(getApparentDirection(state)));
    }

    /**
     * Return the area of the face that is at the edge of the block.
     */
    public static FaceShape getFaceShape(BlockState state, Direction face) {
        SlabType slab = state.getValue(SLAB_TYPE);

        if (state.getValue(AXIS) != face.getAxis()) {
            return FaceShape.forSlabSide(slab);
        }

        return switch (slab) {
            case TOP -> FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.POSITIVE);
            case BOTTOM -> FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.NEGATIVE);
            case DOUBLE -> FaceShape.FULL;
        };
    }

    public static Direction getApparentDirection(BlockState state) {
        return Direction.fromAxisAndDirection(state.getValue(AXIS), state.getValue(SLAB_TYPE) == SlabType.BOTTOM ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE);
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        SlabType type = state.getValue(SLAB_TYPE);
        if (type == SlabType.DOUBLE) {
            return state.setValue(AXIS, direction.getAxis());
        }
        if (getApparentDirection(state).getAxisDirection() != direction.getAxisDirection()) {
            return state.setValue(AXIS, direction.getAxis()).setValue(SLAB_TYPE, type == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
        } else {
            return state.setValue(AXIS, direction.getAxis());
        }
    }

    private enum FaceShape {
        FULL,
        TOP,
        BOTTOM,
        NONE;

        public static FaceShape forSlabSide(SlabType type) {
            return switch (type) {
                case TOP -> TOP;
                case BOTTOM -> BOTTOM;
                case DOUBLE -> FULL;
            };
        }

        public static FaceShape fullOrNone(boolean value) {
            return value ? FULL : NONE;
        }

        public static boolean canConnect(FaceShape shape1, FaceShape shape2) {
            return shape1 == shape2 || shape1 == FaceShape.FULL && shape2 != FaceShape.NONE || shape2 == FaceShape.FULL && shape1 != FaceShape.NONE;
        }

        public boolean hasContact() {
            return this != NONE;
        }
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return CCBlocks.COPYCAT_SLAB::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return CCBlocks.COPYCAT_SLAB::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                    state.getValue(AXIS),
                    dir -> world.getBlockState(pos.relative(dir))
                            .getMaterial().isReplaceable());

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                if (state.getValue(SLAB_TYPE).equals(SlabType.DOUBLE)) {
                    return PlacementOffset.success(pos.relative(directions.get(0)),
                            s -> s.setValue(AXIS, state.getValue(AXIS)).setValue(SLAB_TYPE, SlabType.BOTTOM));
                } else {
                    return PlacementOffset.success(pos.relative(directions.get(0)),
                            s -> s.setValue(AXIS, state.getValue(AXIS)).setValue(SLAB_TYPE, state.getValue(SLAB_TYPE)));
                }
            }
        }
    }

}
