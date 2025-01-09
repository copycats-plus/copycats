package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatSlidingDoorBlock extends SlidingDoorBlock implements ICopycatBlock {

    public static BooleanProperty CT = CopycatDoorBlock.CT;

    public CopycatSlidingDoorBlock(Properties properties, BlockSetType type, boolean folds) {
        super(properties, type, folds);
        registerDefaultState(defaultBlockState().setValue(CT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(CT));
    }

    public static CopycatSlidingDoorBlock metal(Properties properties, boolean folds) {
        return new CopycatSlidingDoorBlock(properties, TRAIN_SET_TYPE.get(), folds);
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.onSneakWrenched(state, context),
                () -> super.onSneakWrenched(state, context)
        );
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.onWrenched(state, context),
                () -> super.onWrenched(state, context)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.use(state, world, pos, player, hand, ray),
                () -> super.use(state, world, pos, player, hand, ray)
        );
    }

    @Override
    public InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            if (!canToggleCT(pState, pLevel, pPos))
                return InteractionResult.PASS;
            pLevel.setBlock(pPos, pState.cycle(CT), 3);
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity fbe))
                return InteractionResult.PASS;
            BlockEntityUtils.redraw((BlockEntity) fbe);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        getBlockEntityOptional(pLevel, pPos).ifPresent(be -> {
            if (be instanceof CopycatSlidingDoorBlockEntity copycatBE)
                copycatBE.updatePaired();
        });
    }

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        return ICopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        ICopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        ICopycatBlock.super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving, super::onRemove);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        ICopycatBlock.super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos, BlockState toState) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        BlockPos toPos = pos.relative(dir);
        BlockState toState = level.getBlockState(toPos);
        BlockState material = state.getBlock() instanceof ICopycatBlock
                ? ICopycatBlock.getMaterial(level, pos)
                : state;
        BlockState neighborMaterial = neighborState.getBlock() instanceof ICopycatBlock
                ? ICopycatBlock.getMaterial(level, toPos)
                : neighborState;
        if (AllBlocks.COPYCAT_BASE.has(neighborMaterial) && AllBlocks.COPYCAT_BASE.has(material)) {
            if (dir == Direction.UP && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.UPPER) {
                return true;
            }
            if (dir == Direction.DOWN && toState.is(this) && toState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                return true;
            }
        }
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntityType().create(pos, state);
    }

    @Override
    public BlockEntityType<? extends CopycatSlidingDoorBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_SLIDING_DOOR.get();
    }
}
