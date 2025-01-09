package com.copycatsplus.copycats.content.copycat.half_panel;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.copycatsplus.copycats.utility.BlockUtils.transformFacing;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatHalfPanelBlock extends CCWaterloggedCopycatBlock implements IStateType {

    /**
     * The direction where the base of the half panel is facing.
     */
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    /**
     * The direction of the half panel within the block face.
     */
    public static final DirectionProperty OFFSET = DirectionProperty.create("offset", Direction.Plane.HORIZONTAL);

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatHalfPanelBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(OFFSET, Direction.NORTH)
        );
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult ray) {
        return InteractionUtils.sequential(
                () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
                () -> super.use(state, world, pos, player, hand, ray)
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;

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
        return CCShapes.HALF_PANEL.get(pState.getValue(FACING)).get(pState.getValue(OFFSET)).toShape();
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);
        Vec3i offsetNormal = transform.applyWithoutOffset(new BlockPos(getOffsetFacing(facing, offset).getNormal()));
        Direction newFacing = transformFacing(transform, facing);
        Vec3i facingNormal = newFacing.getNormal();
        if (offsetNormal.getY() != 0) {
            if (offsetNormal.getX() == 0 && facingNormal.getX() != 0) {
                offsetNormal = new Vec3i(offsetNormal.getY(), offsetNormal.getX(), offsetNormal.getZ());
            } else {
                offsetNormal = new Vec3i(offsetNormal.getX(), offsetNormal.getZ(), offsetNormal.getY());
            }
        }
        return state
                .setValue(FACING, newFacing)
                .setValue(OFFSET, Objects.requireNonNull(Direction.fromDelta(offsetNormal.getX(), offsetNormal.getY(), offsetNormal.getZ())));
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
        return Objects.requireNonNull(Direction.fromDelta(offsetNormal.getX(), offsetNormal.getY(), offsetNormal.getZ()));
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

                if (newState.canBeReplaced())
                    return PlacementOffset.success(newPos, bState -> bState.setValue(property, state.getValue(property)).setValue(OFFSET, state.getValue(OFFSET)));

            }

            return PlacementOffset.fail();
        }
    }
}
