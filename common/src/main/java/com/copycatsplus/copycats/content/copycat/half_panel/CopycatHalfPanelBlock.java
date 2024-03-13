package com.copycatsplus.copycats.content.copycat.half_panel;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static com.copycatsplus.copycats.content.copycat.MathHelper.DirectionFromDelta;

public class CopycatHalfPanelBlock extends CTWaterloggedCopycatBlock {

    /**
     * The direction where the base of the half panel is facing.
     */
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    /**
     * The direction of the half panel within the block face.
     */
    public static final DirectionProperty OFFSET = DirectionProperty.create("offset", Direction.Plane.HORIZONTAL);

    private static final Map<Direction, VoxelShaper> SHAPE_BY_OFFSET = new HashMap<>();

    static {
        SHAPE_BY_OFFSET.put(Direction.NORTH, CCShapes.HALF_PANEL_NORTH);
        SHAPE_BY_OFFSET.put(Direction.SOUTH, CCShapes.HALF_PANEL_SOUTH);
        SHAPE_BY_OFFSET.put(Direction.EAST, CCShapes.HALF_PANEL_EAST);
        SHAPE_BY_OFFSET.put(Direction.WEST, CCShapes.HALF_PANEL_WEST);
    }

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatHalfPanelBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(OFFSET, Direction.NORTH)
        );
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
        Direction direction = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            // connecting to another copycat beam
            return toState.getValue(FACING) != direction || toState.getValue(OFFSET) != offset;
        } else {
            // doesn't connect to any other blocks
            return true;
        }
    }

    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);
        BlockState toState = reader.getBlockState(toPos);

        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = DirectionFromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            return false;
        }

        if (toState.is(this)) {
            return toState.getValue(FACING) == facing && toState.getValue(OFFSET) == offset && face.getAxis() == getOffsetAxis(facing, offset);
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return (canConnectTexturesToward(reader, fromPos, toPos, reader.getBlockState(fromPos)) ? getMaterial(reader, toPos) : null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);
        return face == facing || face == getOffsetFacing(facing, offset) || face.getAxis() == getOffsetAxis(facing, offset);
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;

        Direction facing = context.getClickedFace().getOpposite();
        double offset1, offset2;
        switch (facing.getAxis()) {
            case X -> {
                offset1 = context.getClickLocation().y - context.getClickedPos().getY();
                offset2 = context.getClickLocation().z - context.getClickedPos().getZ();
            }
            case Y -> {
                offset1 = context.getClickLocation().x - context.getClickedPos().getX();
                offset2 = context.getClickLocation().z - context.getClickedPos().getZ();
            }
            case Z -> {
                offset1 = context.getClickLocation().x - context.getClickedPos().getX();
                offset2 = context.getClickLocation().y - context.getClickedPos().getY();
            }
            default -> {
                offset1 = 0;
                offset2 = 0;
            }
        }

        Direction offset;
        if (Math.abs(offset1 - 0.5) > Math.abs(offset2 - 0.5)) {
            offset = Direction.fromAxisAndDirection(
                    Axis.X,
                    offset1 > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE
            );
        } else {
            offset = Direction.fromAxisAndDirection(
                    Axis.Z,
                    offset2 > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE
            );
        }

        return stateForPlacement.setValue(FACING, facing).setValue(OFFSET, offset);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, OFFSET));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_OFFSET.get(pState.getValue(OFFSET)).get(pState.getValue(FACING));
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                return neighborState.getValue(FACING) == state.getValue(FACING) &&
                        neighborState.getValue(OFFSET) == state.getValue(OFFSET) &&
                        getOffsetAxis(state.getValue(FACING), state.getValue(OFFSET)) == dir.getAxis();
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        Axis mirrorAxis = null;
        for (Axis axis : Iterate.axes) {
            if (pMirror.rotation().inverts(axis)) {
                mirrorAxis = axis;
                break;
            }
        }
        if (mirrorAxis == null) {
            return super.mirror(pState, pMirror);
        }
        Direction facing = pState.getValue(FACING);
        Direction offset = pState.getValue(OFFSET);
        if (facing.getAxis() == mirrorAxis) {
            return pState.setValue(FACING, facing.getOpposite());
        } else if (offset.getAxis() == mirrorAxis) {
            return pState.setValue(OFFSET, offset.getOpposite());
        } else {
            return pState;
        }
    }

    /**
     * Get the direction where the front of the half panel touches another block.
     */
    public static Direction getOffsetFacing(Direction facing, Direction offset) {
        if (offset.getAxis().isVertical()) throw new IllegalArgumentException("offset must be a horizontal direction.");

        Vec3i facingNormal = facing.getNormal();
        Vec3i offsetNormal = offset.getNormal();
        if (facingNormal.getX() != 0 && offsetNormal.getX() != 0) {
            offsetNormal = new Vec3i(offsetNormal.getY(), offsetNormal.getX(), offsetNormal.getZ());
        }
        if (facingNormal.getZ() != 0 && offsetNormal.getZ() != 0) {
            offsetNormal = new Vec3i(offsetNormal.getX(), offsetNormal.getZ(), offsetNormal.getY());
        }
        return Objects.requireNonNull(DirectionFromDelta(offsetNormal.getX(), offsetNormal.getY(), offsetNormal.getZ()));
    }

    /**
     * Get the axis in which half panels can form a line.
     */
    public static Axis getOffsetAxis(Direction facing, Direction offset) {
        Direction offsetFacing = getOffsetFacing(facing, offset);
        if (facing.getAxis().isVertical()) return offsetFacing.getClockWise().getAxis();
        if (offsetFacing.getAxis().isVertical()) return facing.getClockWise().getAxis();
        else return Axis.Y;
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper extends PoleHelper<Direction> {

        private PlacementHelper() {
            super(CCBlocks.COPYCAT_HALF_PANEL::has, state -> getOffsetAxis(state.getValue(FACING), state.getValue(OFFSET)), FACING);
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && (((BlockItem) i.getItem()).getBlock() instanceof CopycatHalfPanelBlock);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistance(pos, ray.getLocation(), dir -> dir.getAxis() == axisFunction.apply(state));
            for (Direction dir : directions) {
                int range = AllConfigs.server().equipment.placementAssistRange.get();
                if (player != null) {
                    //TODO: Add way to get reach attribute from platform
                    AttributeInstance reach = null;
                    if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier))
                        range += 4;
                }
                int poles = attachedPoles(world, pos, dir);
                if (poles >= range)
                    continue;

                BlockPos newPos = pos.relative(dir, poles + 1);
                BlockState newState = world.getBlockState(newPos);

                if (newState.getMaterial().isReplaceable())
                    return PlacementOffset.success(newPos, bState -> bState.setValue(property, state.getValue(property)).setValue(OFFSET, state.getValue(OFFSET)));

            }

            return PlacementOffset.fail();
        }
    }
}
