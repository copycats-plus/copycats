package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.copycatsplus.copycats.content.copycat.ShimCopycatBlock;
import com.simibubi.create.AllTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static net.minecraft.world.level.block.ButtonBlock.*;

@SuppressWarnings("deprecation")
public class CopycatStoneButtonBlock extends ShimCopycatBlock implements ICopycatWithWrappedBlock<WrappedButton.Stone> {

    public static WrappedButton.Stone button;

    public CopycatStoneButtonBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(FACE, AttachFace.WALL));
    }

    @Override
    public WrappedButton.Stone getWrappedBlock() {
        return button;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING).add(POWERED).add(FACE));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return button.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState pState) {
        return button.isSignalSource(pState);
    }

    @Override
    public int getSignal(@NotNull BlockState pBlockState, @NotNull BlockGetter pBlockAccess, @NotNull BlockPos pPos, @NotNull Direction pSide) {
        return button.getSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    public int getDirectSignal(@NotNull BlockState pBlockState, @NotNull BlockGetter pBlockAccess, @NotNull BlockPos pPos, @NotNull Direction pSide) {
        return button.getDirectSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (result == InteractionResult.PASS && !pPlayer.getItemInHand(pHand).is(AllTags.AllItemTags.WRENCH.tag)) {
            return button.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return result;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState state = getWrappedBlock().getStateForPlacement(pContext);
        if (state == null) return super.getStateForPlacement(pContext);
        return copyState(state, super.getStateForPlacement(pContext));
    }

    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Random pRandom) {
        button.tick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        button.entityInside(pState, pLevel, pPos, pEntity);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        return button.rotate(pState, pRotation);
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        return button.mirror(pState, pMirror);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        button.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    public BlockState copyState(BlockState from, BlockState to) {
        return to
                .setValue(FACING, from.getValue(FACING))
                .setValue(FACE, from.getValue(FACE))
                .setValue(POWERED, from.getValue(POWERED));
    }

}
