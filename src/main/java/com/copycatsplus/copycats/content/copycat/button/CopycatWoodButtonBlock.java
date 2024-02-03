package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.ButtonBlock.*;

public class CopycatWoodButtonBlock extends CopycatBlock implements ICopycatWithWrappedBlock<WrappedWoodButtonBlock> {

    public static WrappedWoodButtonBlock button;

    public CopycatWoodButtonBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(FACE, AttachFace.WALL));
    }

    @Override
    public WrappedWoodButtonBlock getWrappedBlock() {
        return button;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING).add(POWERED).add(FACE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return button.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return button.isSignalSource(pState);
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return button.getSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return button.getDirectSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (result == InteractionResult.PASS && !pPlayer.getItemInHand(pHand).is(AllTags.AllItemTags.WRENCH.tag)) {
            if (pState.getValue(POWERED)) {
                return InteractionResult.CONSUME;
            } else {
                this.press(pState, pLevel, pPos);
                button.playSound(pPlayer, pLevel, pPos, true);
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_ACTIVATE, pPos);
                return InteractionResult.sidedSuccess(pLevel.isClientSide);
            }
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

    private void updateNeighbours(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.updateNeighborsAt(pPos, this);
        pLevel.updateNeighborsAt(pPos.relative(getConnectedDirection(pState).getOpposite()), this);
    }

    protected static Direction getConnectedDirection(BlockState pState) {
        return switch (pState.getValue(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> pState.getValue(FACING);
        };
    }

    public void press(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.valueOf(true)), 3);
        this.updateNeighbours(pState, pLevel, pPos);
        pLevel.scheduleTick(pPos, this, button.ticksToStayPressed);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(POWERED)) {
            checkPressed(pState, pLevel, pPos);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && button.arrowsCanPress && !pState.getValue(POWERED)) {
            checkPressed(pState, pLevel, pPos);
        }
    }

    public void checkPressed(BlockState pState, Level pLevel, BlockPos pPos) {
        AbstractArrow abstractarrow = button.arrowsCanPress ? pLevel.getEntitiesOfClass(AbstractArrow.class, pState.getShape(pLevel, pPos).bounds().move(pPos)).stream().findFirst().orElse(null) : null;
        boolean arrow = abstractarrow != null;
        boolean powered = pState.getValue(POWERED);
        if (arrow != powered) {
            pLevel.setBlock(pPos, pState.setValue(POWERED, arrow), Block.UPDATE_ALL);
            this.updateNeighbours(pState, pLevel, pPos);
            button.playSound(null, pLevel, pPos, arrow);
            pLevel.gameEvent(abstractarrow, arrow ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pPos);
        }

        if (arrow) {
            pLevel.scheduleTick(pPos, this, button.ticksToStayPressed);
        }

    }

    public BlockState copyState(BlockState from, BlockState to) {
        return to
                .setValue(FACING, from.getValue(FACING))
                .setValue(FACE, from.getValue(FACE))
                .setValue(POWERED, from.getValue(POWERED));
    }

}
