package com.copycatsplus.copycats.content.copycat.slice;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.util.DirectionUtils;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopycatSliceBlock extends CTWaterloggedCopycatBlock implements ISpecialBlockItemRequirement {

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    private static final VoxelShaper[] BOTTOM_BY_LAYER = new VoxelShaper[]{CCShapes.EMPTY, CCShapes.SLICE_BOTTOM_2PX, CCShapes.SLICE_BOTTOM_4PX, CCShapes.SLICE_BOTTOM_6PX, CCShapes.SLICE_BOTTOM_8PX, CCShapes.SLICE_BOTTOM_10PX, CCShapes.SLICE_BOTTOM_12PX, CCShapes.SLICE_BOTTOM_14PX, CCShapes.SLICE_BOTTOM_16PX};
    private static final VoxelShaper[] TOP_BY_LAYER = new VoxelShaper[]{CCShapes.EMPTY, CCShapes.SLICE_TOP_2PX, CCShapes.SLICE_TOP_4PX, CCShapes.SLICE_TOP_6PX, CCShapes.SLICE_TOP_8PX, CCShapes.SLICE_TOP_10PX, CCShapes.SLICE_TOP_12PX, CCShapes.SLICE_TOP_14PX, CCShapes.SLICE_TOP_16PX};

    public CopycatSliceBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(HALF, Half.BOTTOM)
                .setValue(FACING, Direction.SOUTH)
                .setValue(LAYERS, 1)
        );
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            // connecting to another copycat beam
            Direction facing = state.getValue(FACING);
            Half half = state.getValue(HALF);
            int layers = state.getValue(LAYERS);
            return toState.getValue(FACING) != facing || toState.getValue(HALF) != half || toState.getValue(LAYERS) != layers;
        } else {
            // doesn't connect to any other blocks
            return true;
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = DirectionUtils.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            return false;
        }

        Direction facing = state.getValue(FACING);
        Half half = state.getValue(HALF);
        int layers = state.getValue(LAYERS);
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            return toState.getValue(FACING) == facing &&
                    toState.getValue(HALF) == half &&
                    toState.getValue(LAYERS) == layers &&
                    face.getAxis() == facing.getClockWise().getAxis();
        } else {
            return false;
        }
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        if (face.getAxis() == Axis.Y)
            return (state.getValue(HALF) == Half.TOP) == (face == Direction.UP);
        return state.getValue(FACING) != face.getOpposite();
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            if (state.getValue(LAYERS) < 8)
                return state.cycle(LAYERS);
            else {
                Copycats.LOGGER.warn("Can't figure out where to place a slice! Please file an issue if you see this.");
                return state;
            }
        } else {
            Direction facing = context.getHorizontalDirection();
            if (facing.getAxis() == Axis.X) {
                if ((facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) !=
                        (context.getClickLocation().x - context.getClickedPos().getX() > 0.5))
                    facing = facing.getOpposite();
            } else {
                if ((facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) !=
                        (context.getClickLocation().z - context.getClickedPos().getZ() > 0.5))
                    facing = facing.getOpposite();
            }
            stateForPlacement = stateForPlacement.setValue(FACING, facing);
            Direction direction = context.getClickedFace();
            if (direction == Direction.UP)
                return stateForPlacement;
            if (direction == Direction.DOWN || (context.getClickLocation().y - context.getClickedPos()
                    .getY() > 0.5D))
                return stateForPlacement.setValue(HALF, Half.TOP);
            return stateForPlacement;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        if (pState.getValue(LAYERS) == 8) return false;
        Half half = pState.getValue(HALF);
        if (half == Half.TOP && pUseContext.getClickedFace() == Direction.DOWN || half == Half.BOTTOM && pUseContext.getClickedFace() == Direction.UP)
            return true;
        if (pUseContext.getClickedFace() == pState.getValue(FACING).getOpposite())
            return true;
        return false;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(LAYERS) <= 1)
            return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel serverLevel) {
            if (player != null && !player.isCreative()) {
                // Respect loot tables
                List<ItemStack> drops = Block.getDrops(state.setValue(LAYERS, 1), serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            // need to call updateShape before setBlock to schedule a tick for water
            world.setBlockAndUpdate(pos, state.setValue(LAYERS, state.getValue(LAYERS) - 1).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), state.getValue(LAYERS))
        );
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(HALF, FACING, LAYERS));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return (pState.getValue(HALF) == Half.BOTTOM ? BOTTOM_BY_LAYER : TOP_BY_LAYER)[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)
                && getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
            int layers = state.getValue(LAYERS);
            int neighborLayers = neighborState.getValue(LAYERS);
            if (layers == 8 && neighborLayers == 8) return true;
            return neighborState.getValue(FACING) == state.getValue(FACING) &&
                    neighborState.getValue(HALF) == state.getValue(HALF) &&
                    layers == neighborLayers &&
                    state.getValue(FACING).getClockWise().getAxis() == dir.getAxis();
        }
        return false;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}

