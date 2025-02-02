package com.copycatsplus.copycats.content.copycat.flat_pane;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatFlatPaneBlock extends CCWaterloggedCopycatBlock implements IStateType, ICustomCTBlocking {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatFlatPaneBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(AXIS, Axis.Y));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                          @NotNull BlockHitResult ray) {
        return InteractionUtils.sequential(
                () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
                () -> super.use(state, world, pos, player, hand, ray)
        );
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return switch (pType) {
            case LAND -> pState.getValue(AXIS).isHorizontal();
            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        Axis axis = context.getNearestLookingDirection().getAxis();
        return stateForPlacement.setValue(AXIS, axis);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.HORIZONTAL_PANE.get(pState.getValue(AXIS)).toShape();
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.rotationAxis != null) {
            state = state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
        }
        return state;
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, BlockPos toPos, BlockState toState) {
        toState = reader.getBlockState(toPos);
        Vec3i diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO))
            return false;
        Direction facing = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (toState.getBlock() instanceof IronBarsBlock) {
            if (facing == null)
                return true;
            return face.getAxis().isVertical() || fromState.getValue(AXIS) == facing.getAxis();
        } else if (toState.is(this)) {
            if (facing == null)
                return true;
            return toState.getValue(AXIS) != fromState.getValue(AXIS) || fromState.getValue(AXIS) == facing.getAxis();
        }
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockState toState = reader.getBlockState(toPos);
        Vec3i diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO))
            return true;
        Direction facing = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (toState.getBlock() instanceof IronBarsBlock) {
            if (facing == null)
                return false;
            return fromState.getValue(AXIS) != facing.getAxis();
        } else if (toState.is(this)) {
            if (facing == null)
                return false;
            return toState.getValue(AXIS) == fromState.getValue(AXIS) && fromState.getValue(AXIS) != facing.getAxis();
        }
        return false;
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.of(false);
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        return Optional.of(false);
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

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return CCBlocks.COPYCAT_FLAT_PANE::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return CCBlocks.COPYCAT_FLAT_PANE::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                    state.getValue(AXIS),
                    dir -> world.getBlockState(pos.relative(dir)).canBeReplaced());

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                return PlacementOffset.success(pos.relative(directions.get(0)), s -> s.setValue(AXIS, state.getValue(AXIS)));
            }
        }
    }

}
