package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.CCWaterloggedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.core.Direction.UP;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatSlopeLayerBlock extends CCWaterloggedCopycatBlock implements ISpecialBlockItemRequirement, IStateType {


    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    public CopycatSlopeLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(LAYERS, 1)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, HALF, LAYERS));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            if (state.getValue(LAYERS) < 8)
                return state.cycle(LAYERS);
            else {
                Copycats.LOGGER.warn("Can't figure out where to place a slope layer! Please file an issue if you see this.");
                return state;
            }
        } else {
            Half half = context.getClickedFace() == Direction.DOWN
                    ? Half.TOP
                    : context.getClickedFace() == Direction.UP
                    ? Half.BOTTOM
                    : context.getClickLocation().y - context.getClickedPos().getY() > 0.5
                    ? Half.TOP
                    : Half.BOTTOM;
            return stateForPlacement
                    .setValue(FACING, context.getHorizontalDirection())
                    .setValue(HALF, half);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        if (pState.getValue(LAYERS) == 8) return false;
        Half half = pState.getValue(HALF);
        Direction facing = pState.getValue(FACING);
        if (pUseContext.getClickedFace() == facing.getOpposite()) return true;
        if (half == Half.TOP && pUseContext.getClickedFace() == Direction.DOWN) return true;
        if (half == Half.BOTTOM && pUseContext.getClickedFace() == Direction.UP) return true;
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
        return ICopycatBlock.getRequiredItemsForLayer(state, LAYERS);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        Direction facing = state.getValue(FACING);
        BlockState toState = reader.getBlockState(toPos);

        return !toState.is(this);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;
        Direction facing = state.getValue(FACING);

        if (toPos.equals(fromPos.relative(facing))) return false;

        BlockPos diff = fromPos.subtract(toPos);
        int coord = facing.getAxis().choose(diff.getX(), diff.getY(), diff.getZ());

        if (!toState.is(this)) return coord != -facing.getAxisDirection().getStep();

        if (isOccluded(state, toState, facing)) return true;
        if (toState.setValue(WATERLOGGED, false) == state.setValue(WATERLOGGED, false) && coord == 0) return true;
        return false;
    }

    private static boolean isOccluded(BlockState state, BlockState other, Direction pDirection) {
        state = state.setValue(WATERLOGGED, false);
        other = other.setValue(WATERLOGGED, false);
        Direction facing = state.getValue(FACING);
        if (facing.getOpposite() == other.getValue(FACING) && pDirection == facing) return true;
        if (other.getValue(FACING) != facing) return false;
        return pDirection.getAxis() != facing.getAxis();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return switch (pType) {
            case LAND -> pState.getValue(LAYERS) < 5 && pState.getValue(FACING).equals(UP);
            default -> false;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return CCShapes.SLOPE_LAYER.get(pState.getValue(FACING)).get(pState.getValue(HALF)).get(pState.getValue(LAYERS)).toShape();
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
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LAYERS) == 8 ? 0.2f : 1.0f;
    }
}
