package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

/**
 * An interface with implementation for all copycats. Simple copycats should implement this interface while multi-state
 * copycats should implement {@link IMultiStateCopycatBlock}.
 * <p>
 * Implementors should implement
 * {@link net.minecraft.world.level.block.EntityBlock#getTicker},
 * {@link IBE#getBlockEntityClass} and
 * {@link IBE#getBlockEntityType} in the concrete class, and redirect calls of
 * {@link ICopycatBlock#use},
 * {@link ICopycatBlock#hidesNeighborFace},
 * {@link ICopycatBlock#setPlacedBy},
 * {@link ICopycatBlock#onRemove} and
 * {@link ICopycatBlock#playerWillDestroy} to this interface.
 * <p>
 * If the concrete class is not a subclass of {@link CCCopycatBlock},
 * it should also be registered in platform-specific CopycatBlockMixins as a mixin target.
 * <p>
 * It is not recommended to override undocumented methods in this interface, since they are considered internal to
 * the implementation of copycats.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICopycatBlock extends IWrenchable, IStateType, ITransformableBlock {

    @Nullable
    default ICopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);

        if (blockEntity == null)
            return null;
        if (!(blockEntity instanceof ICopycatBlockEntity copycatBE))
            return null;

        return copycatBE;
    }

    /**
     * Controls whether the player can shift-R-click on this block to toggle CT.
     */
    default boolean canToggleCT(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return true;
    }

    default boolean isCTEnabled(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ICopycatBlockEntity fbe))
            return true;
        if (!canToggleCT(state, level, pos))
            return true;
        return fbe.isCTEnabled();
    }

    default InteractionResult toggleCT(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            if (!canToggleCT(pState, pLevel, pPos))
                return InteractionResult.PASS;
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (!(be instanceof ICopycatBlockEntity fbe))
                return InteractionResult.PASS;
            if (!canToggleCT(pState, pLevel, pPos))
                return InteractionResult.PASS;
            fbe.setCTEnabled(!fbe.isCTEnabled());
            BlockEntityUtils.redraw((BlockEntity) fbe);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (copycatBE == null)
            return InteractionResult.PASS;
        ItemStack consumedItem = copycatBE.getConsumedItem();
        if (!copycatBE.hasCustomMaterial())
            return InteractionResult.PASS;
        Player player = context.getPlayer();
        if (!player.isCreative())
            player.getInventory()
                    .placeItemBackInInventory(consumedItem);
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(getMaterial(context.getLevel(), context.getClickedPos())));
        copycatBE.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
        copycatBE.setConsumedItem(ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    default BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (!(item.getItem() instanceof BlockItem bi))
            return null;

        Block block = bi.getBlock();
        if (block instanceof ICopycatBlock)
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

    /**
     * Determines whether the material is accepted regardless of COPYCAT_ALLOW and COPYCAT_DENY tags.
     */
    default boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    /**
     * Modifies an accepted material before it is stored and displayed on the copycat.
     */
    default BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer,
                                       InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    default InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        // prioritize wrench interactions over others
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag)) {
            InteractionResult result = AllItems.WRENCH.get().useOn(new UseOnContext(player, hand, ray));
            if (result.consumesAction())
                return result;
        }

        InteractionResult result = toggleCT(state, world, pos, player, hand, ray);
        if (result.consumesAction())
            return result;

        if (player == null || !player.mayBuild())
            return InteractionResult.PASS;

        Direction face = ray.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState materialIn = getAcceptedBlockState(world, pos, itemInHand, face);

        if (materialIn != null)
            materialIn = prepareMaterial(world, pos, state, player, hand, ray, materialIn);
        if (materialIn == null)
            return InteractionResult.PASS;

        BlockState material = materialIn;
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(world, pos);
        if (copycatBE == null)
            return InteractionResult.PASS;
        if (copycatBE.getMaterial()
                .is(material.getBlock())) {
            if (!copycatBE.cycleMaterial())
                return InteractionResult.PASS;
            copycatBE.getLevel()
                    .playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                            .95f);
            return InteractionResult.SUCCESS;
        }
        if (copycatBE.hasCustomMaterial())
            return InteractionResult.PASS;
        if (world.isClientSide())
            return InteractionResult.SUCCESS;

        copycatBE.setMaterial(material);
        copycatBE.setConsumedItem(itemInHand);
        copycatBE.getLevel()
                .playSound(null, copycatBE.getBlockPos(), material.getSoundType()
                        .getPlaceSound(), SoundSource.BLOCKS, 1, .75f);

        if (player.isCreative())
            return InteractionResult.SUCCESS;

        itemInHand.shrink(1);
        if (itemInHand.isEmpty())
            player.setItemInHand(hand, ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    default void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer == null)
            return;
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(worldIn, pos, offhandItem, Direction.orderedByNearest(placer)[0]);

        if (appliedState == null)
            return;
        ICopycatBlockEntity copycatBE = getCopycatBlockEntity(worldIn, pos);
        if (copycatBE == null)
            return;
        if (copycatBE.hasCustomMaterial())
            return;

        copycatBE.setMaterial(appliedState);
        copycatBE.setConsumedItem(offhandItem);

        if (placer instanceof Player player && player.isCreative())
            return;
        offhandItem.shrink(1);
        if (offhandItem.isEmpty())
            placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    /**
     * Implementation note: must be called before super.remove
     */
    default void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        if (!isMoving) {
            ICopycatBlockEntity copycatBE = getCopycatBlockEntity(world, pos);
            if (copycatBE != null)
                Block.popResource(world, pos, copycatBE.getConsumedItem());
        }
        world.removeBlockEntity(pos);
    }

    default void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            ICopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
            if (copycatBE != null) copycatBE.setConsumedItem(ItemStack.EMPTY);
        }
    }

    static BlockState getAppearance(ICopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {
        if (block.isIgnoredConnectivitySide(level, state, side, pos, queryPos))
            return state;

        BlockState material = getMaterial(level, pos);
        return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    /**
     * Get the material of the copycat at the given position.
     * If the copycat is multi-state, the material for the default part is returned.
     */
    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        if (reader.getBlockEntity(targetPos) instanceof ICopycatBlockEntity cbe)
            return cbe.getMaterial();
        return Blocks.AIR.defaultBlockState();
    }

    /**
     * Transform the block state of the copycat according to the provided transform.
     * <p>
     * Possible transforms include single-axis rotation of 90 degree increments and mirroring in any axis.
     */
    @Override
    default BlockState transform(BlockState state, StructureTransform transform) {
        Direction.Axis rotationAxis = transform.rotationAxis;
        Rotation rotation = transform.rotation;
        Mirror mirror = transform.mirror;

        Block block = state.getBlock();

        if (mirror != null)
            state = state.mirror(mirror);

        if (rotationAxis == Direction.Axis.Y) {
            if (block instanceof BellBlock) {
                if (state.getValue(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.DOUBLE_WALL)
                    state = state.setValue(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.SINGLE_WALL);
                return state.setValue(BellBlock.FACING,
                        rotation.rotate(state.getValue(BellBlock.FACING)));
            }

            return state.rotate(rotation);
        }

        if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
            DirectionProperty facingProperty = FaceAttachedHorizontalDirectionalBlock.FACING;
            EnumProperty<AttachFace> faceProperty = FaceAttachedHorizontalDirectionalBlock.FACE;
            Direction stateFacing = state.getValue(facingProperty);
            AttachFace stateFace = state.getValue(faceProperty);
            boolean z = rotationAxis == Direction.Axis.Z;
            Direction forcedAxis = z ? Direction.WEST : Direction.SOUTH;

            if (stateFacing.getAxis() == rotationAxis && stateFace == AttachFace.WALL)
                return state;

            for (int i = 0; i < rotation.ordinal(); i++) {
                stateFace = state.getValue(faceProperty);
                stateFacing = state.getValue(facingProperty);

                boolean b = state.getValue(faceProperty) == AttachFace.CEILING;
                state = state.setValue(facingProperty, b ? forcedAxis : forcedAxis.getOpposite());

                if (stateFace != AttachFace.WALL) {
                    state = state.setValue(faceProperty, AttachFace.WALL);
                    continue;
                }

                if (stateFacing.getAxisDirection() == (z ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE)) {
                    state = state.setValue(faceProperty, AttachFace.FLOOR);
                    continue;
                }
                state = state.setValue(faceProperty, AttachFace.CEILING);
            }

            return state;
        }

        boolean halfTurn = rotation == Rotation.CLOCKWISE_180;
        if (block instanceof StairBlock) {
            if (state.getValue(StairBlock.FACING)
                    .getAxis() != rotationAxis) {
                for (int i = 0; i < rotation.ordinal(); i++) {
                    Direction direction = state.getValue(StairBlock.FACING);
                    Half half = state.getValue(StairBlock.HALF);
                    if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ half == Half.BOTTOM
                            ^ direction.getAxis() == Direction.Axis.Z)
                        state = state.cycle(StairBlock.HALF);
                    else
                        state = state.setValue(StairBlock.FACING, direction.getOpposite());
                }
            } else {
                if (halfTurn) {
                    state = state.cycle(StairBlock.HALF);
                }
            }
            return state;
        }

        if (state.hasProperty(FACING)) {
            state = state.setValue(FACING, transform.rotateFacing(state.getValue(FACING)));
        } else if (state.hasProperty(AXIS)) {
            state = state.setValue(AXIS, transform.rotateAxis(state.getValue(AXIS)));
        } else if (halfTurn) {
            if (state.hasProperty(HORIZONTAL_FACING)) {
                Direction stateFacing = state.getValue(HORIZONTAL_FACING);
                if (stateFacing.getAxis() == rotationAxis)
                    return state;
            }

            state = state.rotate(rotation);

            if (state.hasProperty(SlabBlock.TYPE) && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE)
                state = state.setValue(SlabBlock.TYPE,
                        state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
        }

        return state;
    }

    /**
     * Determines whether textures on an adjacent block should appear connected to the copycat block.
     *
     * @param reader  The world.
     * @param state   The state of the copycat block.
     * @param face    The face of the adjacent block that is being rendered.
     * @param fromPos The position of the copycat block.
     * @param toPos   The position of the adjacent block.
     * @return Whether the adjacent block is not allowed to connect.
     */
    default boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                              BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    /**
     * Determines whether the copycat block can connect its textures towards the adjacent block.
     *
     * @param reader  The world.
     * @param fromPos The position of the copycat block.
     * @param toPos   The position of the adjacent block.
     * @param state   The state of the copycat block.
     * @return Whether the copycat block can connect its textures towards the adjacent block.
     */
    default boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                             BlockState state) {
        return true;
    }

    /**
     * Whether this copycat can occlude faces of adjacent blocks if their shape is fully covered by the copycat.
     *
     * @param level The world.
     * @param state The state of the copycat block.
     * @param pos   The position of the copycat block.
     * @return Whether the copycat can occlude faces of adjacent blocks.
     */
    default boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = getMaterial(level, pos);
        if (AllBlocks.COPYCAT_BASE.has(material)) return false; // copycat_base is incorrectly set to occlude
        return material.canOcclude();
    }

    /**
     * Whether the shape of this copycat can occlude the face of an adjacent block.
     * <p>
     * Implementations of this method should not consider occlusion criteria that are based on the material of the copycat.
     * Only properties intrinsic to the copycat block, such as its shape, should be considered.
     *
     * @param level       The world.
     * @param pos         The position of the copycat block.
     * @param state       The state of the copycat block.
     * @param neighborPos The position of the adjacent block.
     * @param dir         The direction from the copycat block to the adjacent block.
     * @return Whether the shape of the copycat can occlude the face of the adjacent block. If empty, the vanilla occlusion logic is used.
     */
    default Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        return Optional.empty();
    }

    /**
     * Whether this copycat can hide the face of an adjacent block.
     * <p>
     * Note that face hiding is different from occlusion, as it is meant for hiding inner faces of transparent blocks.
     * Face hiding hides the face of the adjacent block if the adjacent block is the same type regardless of whether
     * this block has occlusion enabled.
     */
    static boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        BlockPos toPos = pos.relative(dir);
        if (getMaterial(level, pos).skipRendering(getMaterial(level, toPos), dir.getOpposite())) {
            return BlockFaceUtils.facesMatch(level, neighborState, toPos, state, pos, dir.getOpposite());
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    static BlockColor wrappedColor() {
        return new WrappedBlockColor();
    }

    @Environment(EnvType.CLIENT)
    class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos,
                            int pTintIndex) {
            if (pLevel == null || pPos == null)
                return GrassColor.getDefaultColor();
            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
        }
    }
}
