package com.copycatsplus.copycats.content.copycat.chaining_sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.chaining_door.CopycatChainingDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

public class CopycatChainingSlidingDoorBlock extends CopycatChainingDoorBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatChainingSlidingDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState()
                .setValue(VISIBLE, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(VISIBLE));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        boolean flag = level.hasNeighborSignal(blockpos);
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection())
                .setValue(HINGE, this.getHinge(context))
                .setValue(POWERED, flag)
                .setValue(OPEN, flag);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hitResult) {
        return super.useWithoutItem(state, world, pos, player, hitResult);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return super.mirror(pState, pMirror);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        return super.rotate(pState, pRot);
    }


    private static final int MAX_CHAIN_SIZE = 128;

    private void toggleDoorChain(Level world, BlockPos startPos, BlockState startState) {
        boolean targetOpenState = !startState.getValue(OPEN);

        Queue<BlockPos> toProcess = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();

        toProcess.add(startPos);
        visited.add(startPos);

        while (!toProcess.isEmpty() && visited.size() < MAX_CHAIN_SIZE) {
            BlockPos currentPos = toProcess.poll();
            BlockState currentState = world.getBlockState(currentPos);
            Direction currentFacing = currentState.getValue(FACING);

            world.setBlock(currentPos, currentState.setValue(OPEN, targetOpenState), 3);

            // 1. Find and queue valid VERTICAL neighbors.
            for (Direction verticalDir : new Direction[]{Direction.UP, Direction.DOWN}) {
                BlockPos neighborPos = currentPos.relative(verticalDir);
                if (visited.contains(neighborPos)) continue;

                BlockState neighborState = world.getBlockState(neighborPos);
                if (isValidVerticalNeighbor(currentState, neighborState)) {
                    visited.add(neighborPos);
                    toProcess.add(neighborPos);
                }
            }

            // 2. Find and queue valid HORIZONTAL neighbors for double doors.
            for (Direction horizontalDir : new Direction[]{currentFacing.getClockWise(), currentFacing.getCounterClockWise()}) {
                BlockPos neighborPos = currentPos.relative(horizontalDir);
                if (visited.contains(neighborPos)) continue;

                BlockState neighborState = world.getBlockState(neighborPos);
                if (isValidDoubleDoorPair(currentState, horizontalDir, neighborState)) {
                    visited.add(neighborPos);
                    toProcess.add(neighborPos);
                }
            }
        }
        world.playSound(null, startPos,
                targetOpenState ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE,
                SoundSource.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.1f + 0.9f);
    }

    private DoorHingeSide getHinge(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection();
        Vec3 clickLocation = context.getClickLocation();

        double x = clickLocation.x - pos.getX();
        double z = clickLocation.z - pos.getZ();

        return switch (facing) {
            case EAST -> z < 0.5 ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            case SOUTH -> x > 0.5 ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            case WEST -> z > 0.5 ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            default -> x < 0.5 ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
        };
    }

    private boolean isValidVerticalNeighbor(BlockState currentState, BlockState neighborState) {
        if (!neighborState.is(this)) return false;

        return neighborState.getValue(FACING) == currentState.getValue(FACING)
                && neighborState.getValue(HINGE) == currentState.getValue(HINGE);
    }

    /**
     * Checks if a neighbor forms a valid double door pair, preventing "hinges touching".
     * Rule: Must be the same door type, same facing, opposite hinge, AND
     * the connection must be on the non-hinge side of the current door.
     */
    private boolean isValidDoubleDoorPair(BlockState currentState, Direction checkDirection, BlockState neighborState) {
        if (!neighborState.is(this)) return false;

        if (neighborState.getValue(FACING) != currentState.getValue(FACING)) return false;
        if (neighborState.getValue(HINGE) == currentState.getValue(HINGE)) return false;

        Direction currentFacing = currentState.getValue(FACING);
        DoorHingeSide currentHinge = currentState.getValue(HINGE);

        if (currentHinge == DoorHingeSide.LEFT && checkDirection == currentFacing.getClockWise()) {
            return true;
        }

        if (currentHinge == DoorHingeSide.RIGHT && checkDirection == currentFacing.getCounterClockWise()) {
            return true;
        }
        return false;
    }

    private boolean isValidHorizontalNeighbor(BlockState neighborState, Direction originalFacing, DoorHingeSide currentHinge) {
        return neighborState.is(this)
                && neighborState.getValue(FACING) == originalFacing
                && neighborState.getValue(HINGE) != currentHinge;
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 3.0F);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0F, 0.0F, 13.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape WEST_AABB = Block.box(13.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);
    protected static final VoxelShape EAST_AABB = Block.box(0.0F, 0.0F, 0.0F, 3.0F, 16.0F, 16.0F);

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean  open = !state.getValue(OPEN);
        boolean hinge = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        VoxelShape shape;
        switch (direction) {
            case SOUTH -> shape = open ? SOUTH_AABB : (hinge ? EAST_AABB : WEST_AABB);
            case WEST -> shape = open ? WEST_AABB : (hinge ? SOUTH_AABB : NORTH_AABB);
            case NORTH -> shape = open ? NORTH_AABB : (hinge ? WEST_AABB : EAST_AABB);
            default -> shape = open ? EAST_AABB : (hinge ? NORTH_AABB : SOUTH_AABB);
        }
        return shape;
    }

    public static final class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return i -> i.getItem() instanceof BlockItem
                    && (((BlockItem) i.getItem()).getBlock() instanceof CopycatChainingSlidingDoorBlock);
        }

        @Override
        public @NotNull Predicate<BlockState> getStatePredicate() {
            return state ->  state.is(CCBlocks.COPYCAT_CHAINING_DOOR);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level level, BlockState blockState, BlockPos blockPos, BlockHitResult blockHitResult) {
            return PlacementOffset.success(blockPos.above(), state ->
                    state.setValue(FACING, blockState.getValue(FACING))
                            .setValue(HINGE, blockState.getValue(HINGE))
                            .setValue(OPEN, blockState.getValue(OPEN))
                            .setValue(POWERED, blockState.getValue(POWERED)));
        }
    }
}
