package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Base class for multi-state copycat blocks. Extend this class for multi-state copycat blocks that do not require
 * another base class and cannot be waterlogged.
 * <p>
 * Note: DO NOT check for multi-state copycats with instanceof checks against this class. Copycats may implement
 * {@link IMultiStateCopycatBlock} without extending this class. Check for multi-state copycats with instanceof checks
 * against {@link IMultiStateCopycatBlock} instead.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class MultiStateCopycatBlock extends Block implements IMultiStateCopycatBlock, IBE<MultiStateCopycatBlockEntity> {

    public MultiStateCopycatBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return IMultiStateCopycatBlock.super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        IMultiStateCopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        IMultiStateCopycatBlock.super.onRemove(state, world, pos, newState, isMoving, super::onRemove);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        IMultiStateCopycatBlock.super.playerWillDestroy(level, pos, state, player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        return IMultiStateCopycatBlock.super.rotate(state, rot);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return IMultiStateCopycatBlock.super.mirror(state, mirror);
    }

    @Override
    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT.get();
    }
}
