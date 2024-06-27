package com.copycatsplus.copycats.content.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Indicates that a block functions as a copycat but is not a subclass of {@link CopycatBlock}.
 */
public interface IFunctionalCopycatBlock extends IWrenchable {

    @Nullable
    default IFunctionalCopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);

        if (blockEntity == null)
            return null;
        if (!(blockEntity instanceof IFunctionalCopycatBlockEntity functionalCopycatBlockEntity))
            return null;

        return functionalCopycatBlockEntity;
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        IFunctionalCopycatBlockEntity ufte = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (ufte == null)
            return InteractionResult.PASS;
        ItemStack consumedItem = ufte.getConsumedItem();
        if (!ufte.hasCustomMaterial())
            return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (!player.isCreative())
            player.getInventory()
                    .placeItemBackInInventory(consumedItem);
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(ufte.getBlockState()));
        ufte.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        ufte.setConsumedItem(ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof CopycatBlock || block instanceof IFunctionalCopycatBlock)
            return null;

        BlockState appliedState = block.defaultBlockState();
        boolean hardCodedAllow = isAcceptedRegardless(appliedState);

        if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {

            if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block))
                return null;
            if (block instanceof EntityBlock)
                return null;
            if (block instanceof StairBlock)
                return null;

            if (pLevel != null) {
                VoxelShape shape = appliedState.getShape(pLevel, pPos);
                if (shape.isEmpty() || !shape.bounds()
                        .equals(Shapes.block()
                                .bounds()))
                    return null;

                VoxelShape collisionShape = appliedState.getCollisionShape(pLevel, pPos);
                if (collisionShape.isEmpty())
                    return null;
            }
        }

        if (face != null) {
            Direction.Axis axis = face.getAxis();

            if (appliedState.hasProperty(BlockStateProperties.FACING))
                appliedState = appliedState.setValue(BlockStateProperties.FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_FACING, face);
            if (appliedState.hasProperty(BlockStateProperties.AXIS))
                appliedState = appliedState.setValue(BlockStateProperties.AXIS, axis);
            if (appliedState.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && axis != Direction.Axis.Y)
                appliedState = appliedState.setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
        }

        return appliedState;
    }

    default boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    default BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer,
                                       InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    default InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.isShiftKeyDown() && player.getItemInHand(hand).equals(ItemStack.EMPTY)) {
            IFunctionalCopycatBlockEntity be = getCopycatBlockEntity(world, pos);
            be.setCTEnabled(!be.isCTEnabled());
            be.callRedraw();
            return InteractionResult.SUCCESS;
        }

        if (player == null || player != null && !player.mayBuild() && !player.isSpectator())
            return InteractionResult.PASS;

        Direction face = ray.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = getAcceptedBlockState(world, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(world, pos, state, player, hand, ray, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        IFunctionalCopycatBlockEntity ufte = getCopycatBlockEntity(world, pos);
        if (ufte == null)
            return InteractionResult.PASS;
        if (ufte.getMaterial()
                .is(material.getBlock())) {
            if (!ufte.cycleMaterial())
                return InteractionResult.PASS;
            ufte.getLevel()
                    .playSound(null, ufte.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                            .95f);
            return InteractionResult.SUCCESS;
        }
        if (ufte.hasCustomMaterial())
            return InteractionResult.PASS;
        if (world.isClientSide())
            return InteractionResult.SUCCESS;

        ufte.setMaterial(material);
        ufte.setConsumedItem(itemInHand);
        ufte.getLevel()
                .playSound(null, ufte.getBlockPos(), material.getSoundType()
                        .getPlaceSound(), SoundSource.BLOCKS, 1, .75f);

        if (player.isCreative())
            return InteractionResult.SUCCESS;

        itemInHand.shrink(1);
        if (itemInHand.isEmpty())
            player.setItemInHand(hand, ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    default void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (placer == null)
            return;
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(worldIn, pos, offhandItem, Direction.orderedByNearest(placer)[0]);

        if (appliedState == null)
            return;
        IFunctionalCopycatBlockEntity ufte = getCopycatBlockEntity(worldIn, pos);
        if (ufte == null)
            return;
        if (ufte.hasCustomMaterial())
            return;

        ufte.setMaterial(appliedState);
        ufte.setConsumedItem(offhandItem);

        if (placer instanceof Player player && player.isCreative())
            return;
        offhandItem.shrink(1);
        if (offhandItem.isEmpty())
            placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    default void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        if (!isMoving) {
            IFunctionalCopycatBlockEntity ufte = getCopycatBlockEntity(world, pos);
            if (ufte != null)
                Block.popResource(world, pos, ufte.getConsumedItem());
        }
        world.removeBlockEntity(pos);
    }

    default void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            IFunctionalCopycatBlockEntity ufte = getCopycatBlockEntity(level, pos);
            if (ufte != null) ufte.setConsumedItem(ItemStack.EMPTY);
        }
    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof IFunctionalCopycatBlockEntity cbe)
            return cbe.getMaterial();
        return Blocks.AIR.defaultBlockState();
    }

    default boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                              BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    default boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                             BlockState state) {
        return true;
    }

    default boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    default boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    static BlockColor wrappedColor() {
        return new WrappedBlockColor();
    }

    static class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos,
                            int pTintIndex) {
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }
    }
}
