package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.equipment.extendoGrip.ExtendoGripItem;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

import static net.minecraft.world.level.block.LadderBlock.FACING;
import static net.minecraft.world.level.block.LadderBlock.WATERLOGGED;

public class CopycatLadderBlock extends CopycatBlock implements ICopycatWithWrappedBlock<WrappedLadderBlock>, IStateType {

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());
    public static BooleanProperty RAILS = BooleanProperty.create("rails");
    public static BooleanProperty STEPS = BooleanProperty.create("steps");
    public static WrappedLadderBlock ladder;

    public CopycatLadderBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(RAILS, true)
                .setValue(STEPS, true)
                .setValue(WATERLOGGED, false));
    }

    /* Undoing so i can merge multistate branch into multiloader.
    @Override
    public int maxMaterials() {
        return 2;
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(2, 2, 2);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(RAILS, STEPS).stream().map(BooleanProperty::getName).collect(Collectors.toSet());
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        return switch (property) {
            case "rails" -> state.getValue(RAILS);
            case "steps" -> state.getValue(STEPS);
            default ->
                    throw new AssertionError("This shouldn't appear as there isn't any other properties for the ladder!");
        };
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        Direction stateFacing = state.getValue(FACING);
        Vec3 posAsVec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        BlockHitResult hitResult = level.clip(new ClipContext(posAsVec, posAsVec.relative(stateFacing, 1), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
        if (CCShapes.LADDER_RAILS.get(stateFacing).bounds().contains(hitResult.getLocation())) {
            return RAILS.getName();
        } else if (CCShapes.LADDER_STEPS.get(stateFacing).bounds().contains(hitResult.getLocation())) {
            return STEPS.getName();
        } else {
            return STEPS.getName();
        }
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        if (property.equalsIgnoreCase("rails")) {
            return new Vec3i(1, 0, 0);
        }
        return Vec3i.ZERO;
    }*/

    @Override
    public WrappedLadderBlock getWrappedBlock() {
        return ladder;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING).add(RAILS).add(STEPS).add(WATERLOGGED));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        assert stateForPlacement != null;
        return stateForPlacement.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, BlockPos blockPos1, BlockState blockState) {
        return false;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        if (pState.getValue(RAILS) && !pState.getValue(STEPS)) {
            return CCShapes.LADDER_RAILS.get(facing);
        } else if (pState.getValue(STEPS) && !pState.getValue(RAILS)) {
            return CCShapes.LADDER_STEPS.get(facing);
        } else if (pState.getValue(RAILS) && pState.getValue(STEPS)) {
            return CCShapes.LADDER_BOTH.get(facing);
        } else {
            return ladder.getShape(pState, pLevel, pPos, pContext);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return ladder.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        return ladder.rotate(pState, pRotation);
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return ladder.mirror(pState, pMirror);
    }

    public BlockState copyState(BlockState from, BlockState to) {
        return to.setValue(LadderBlock.FACING, from.getValue(LadderBlock.FACING));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return true;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        InteractionResult result = super.use(state, world, pos, player, hand, ray);
        if (player.isShiftKeyDown() || !player.mayBuild())
            return result;
        ItemStack heldItem = player.getItemInHand(hand);
        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(heldItem))
            return helper.getOffset(player, world, state, pos, ray)
                    .placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
        return result;
    }

/* Undoing so i can merge multistate branch into multiloader
   @Override
    public BlockEntityType<? extends MultiStateCopycatBlockCommonEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT_LADDER_BLOCK_ENTITY.get();
    }*/

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && ((BlockItem) i.getItem()).getBlock() instanceof CopycatLadderBlock;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return s -> s.getBlock() instanceof LadderBlock || s.getBlock() instanceof CopycatLadderBlock;
        }

        public int attachedLadders(Level world, BlockPos pos, Direction direction) {
            BlockPos checkPos = pos.relative(direction);
            BlockState state = world.getBlockState(checkPos);
            int count = 0;
            while (getStatePredicate().test(state)) {
                count++;
                checkPos = checkPos.relative(direction);
                state = world.getBlockState(checkPos);
            }
            return count;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            Direction dir = player.getXRot() < 0 ? Direction.UP : Direction.DOWN;

            int range = AllConfigs.server().equipment.placementAssistRange.get();
            if (player != null) {
                //TODO: get reach from platform
                AttributeInstance reach = null;
                if (reach != null && reach.hasModifier(ExtendoGripItem.singleRangeAttributeModifier))
                    range += 4;
            }

            int ladders = attachedLadders(world, pos, dir);
            if (ladders >= range)
                return PlacementOffset.fail();

            BlockPos newPos = pos.relative(dir, ladders + 1);
            BlockState newState = world.getBlockState(newPos);

            if (!state.canSurvive(world, newPos))
                return PlacementOffset.fail();

            if (newState.canBeReplaced())
                return PlacementOffset.success(newPos, bState -> bState.setValue(FACING, state.getValue(FACING)));
            return PlacementOffset.fail();
        }

    }

}
