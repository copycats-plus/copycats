package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static net.minecraft.core.Direction.*;

public class CopycatLayerBlock extends WaterloggedCopycatBlock implements ISpecialBlockItemRequirement {


    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    private static final VoxelShaper[] SHAPE_BY_LAYER = new VoxelShaper[]{CCShapes.EMPTY, CCShapes.LAYER_2PX, CCShapes.LAYER_4PX, CCShapes.LAYER_6PX, CCShapes.LAYER_8PX, CCShapes.LAYER_10PX, CCShapes.LAYER_12PX, CCShapes.LAYER_14PX, CCShapes.LAYER_16PX};

    public CopycatLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(FACING, UP));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        Direction facing = state.getValue(FACING);
        BlockState toState = reader.getBlockState(toPos);

        return !toState.is(this);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.getItemInHand(pHand).getItem().equals(CCBlocks.COPYCAT_LAYER.asStack().getItem())) {
            if (pState.getValue(LAYERS) < 8) {
                BlockState newState = pState;
                newState = newState.setValue(LAYERS, pState.getValue(LAYERS) + 1);
                pLevel.setBlock(pPos, newState, Block.UPDATE_ALL);
                if (!pPlayer.isCreative()) pPlayer.getItemInHand(pHand).shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(LAYERS) > 1) {
            BlockState newState = state;
            newState = newState.setValue(LAYERS, state.getValue(LAYERS) - 1);
            context.getLevel().setBlock(context.getClickedPos(), newState, Block.UPDATE_ALL);
            assert context.getPlayer() != null;
            if (!context.getPlayer().isCreative())
                context.getPlayer().getInventory().placeItemBackInInventory(CCBlocks.COPYCAT_LAYER.asStack());
            return InteractionResult.SUCCESS;
        }
        return super.onSneakWrenched(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        return stateForPlacement.setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), state.getValue(LAYERS))
        );
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        Direction facing = state.getValue(FACING);
        BlockState toState = reader.getBlockState(toPos);

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

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING).add(LAYERS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }

    public @NotNull VoxelShape getCollisionShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }

    public @NotNull VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }

    public @NotNull VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                return dir.getAxis().isVertical() && neighborState.getValue(FACING) == state.getValue(FACING);
            }
        }

        return false;
    }

}
