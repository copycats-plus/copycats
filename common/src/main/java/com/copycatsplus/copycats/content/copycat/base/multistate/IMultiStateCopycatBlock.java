package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.IStateType;
import com.copycatsplus.copycats.content.copycat.base.StateType;
import com.copycatsplus.copycats.content.copycat.base.model.ScaledBlockAndTintGetter;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.block.IBE;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

/**
 * An interface with implementation for all multi-state copycats.
 * <p>
 * Implementors should implement
 * {@link net.minecraft.world.level.block.EntityBlock#getTicker},
 * {@link IBE#getBlockEntityClass} and
 * {@link IBE#getBlockEntityType} in the concrete class, and redirect calls of
 * {@link IMultiStateCopycatBlock#use},
 * {@link IMultiStateCopycatBlock#setPlacedBy},
 * {@link Block#getShape},
 * {@link IMultiStateCopycatBlock#onRemove} and
 * {@link IMultiStateCopycatBlock#playerWillDestroy} to this interface.
 * <p>
 * If the concrete class is not a subclass of {@link MultiStateCopycatBlock},
 * it should also be registered in platform-specific MultiStateCopycatBlockMixins as a mixin target.
 * <p>
 * It is not recommended to override undocumented methods in this interface, since they are considered internal to
 * the implementation of copycats.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IMultiStateCopycatBlock extends ICopycatBlock, IStateType {

    @Override
    default StateType stateType() {
        return StateType.MULTI;
    }

    @Nullable
    default IMultiStateCopycatBlockEntity getCopycatBlockEntity(BlockGetter worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);

        if (blockEntity == null)
            return null;
        if (!(blockEntity instanceof IMultiStateCopycatBlockEntity copycatBE))
            return null;

        return copycatBE;
    }

    /**
     * When it is impossible to determine the specific part involved in an interaction, this property is used.
     */
    String defaultProperty();

    /**
     * The axis-scale of a virtual render world such that each part of the copycat occupies a single block space.
     */
    Vec3i vectorScale(BlockState state);

    /**
     * The string keys of all parts that can be stored in the copycat.
     */
    Set<String> storageProperties();

    /**
     * Get the color index of a part based on its property. This is used to colorize the copycat such that each
     * adjacent part has a different color for accessibility.
     */
    int getColorIndex(String property);

    /**
     * Whether the given part exists in the copycat based on the given state.
     */
    boolean partExists(BlockState state, String property);

    Vec3i getVectorFromProperty(BlockState state, String property);


    String getPropertyFromInteraction(BlockState state,
                                      BlockGetter level,
                                      Vec3i hitLocation,
                                      BlockPos blockPos,
                                      Direction facing,
                                      Vec3 unscaledHit);

    /**
     * @param targetingSolid Whether the interaction is targeting the solid part behind the block face or the air in front of the block face.
     */
    default String getPropertyFromInteraction(BlockState state,
                                              BlockGetter level,
                                              BlockPos pos,
                                              Vec3 hitVec,
                                              Direction face,
                                              boolean targetingSolid) {
        // Relativize the hit vector around the player position
        if (targetingSolid) {
            hitVec = hitVec.subtract(Vec3.atLowerCornerOf(face.getNormal()).scale(0.05));
        } else {
            hitVec = hitVec.add(Vec3.atLowerCornerOf(face.getNormal()).scale(0.05));
        }
        hitVec = hitVec.add(-pos.getX(), -pos.getY(), -pos.getZ());
        Vec3 unscaledHit = hitVec;
        Vec3i scale = vectorScale(state);
        hitVec = hitVec.multiply(scale.getX(), scale.getY(), scale.getZ());
        BlockPos location = new BlockPos((int) hitVec.x(), (int) hitVec.y(), (int) hitVec.z());
        return getPropertyFromInteraction(state, level, location, pos, face, unscaledHit);
    }

    /**
     * @param targetingSolid Whether the interaction is targeting the solid part behind the block face or the air in front of the block face.
     */
    default String getPropertyFromInteraction(BlockState state,
                                              BlockGetter level,
                                              BlockPos pos,
                                              BlockHitResult hit,
                                              boolean targetingSolid) {
        Vec3 hitVec = hit.getLocation();
        return getPropertyFromInteraction(state, level, pos, hitVec, hit.getDirection(), targetingSolid);
    }

    default String getPropertyFromRender(String renderingProperty,
                                         BlockState state,
                                         ScaledBlockAndTintGetter level,
                                         Vec3i vector,
                                         BlockPos blockPos) {
        Vec3i scale = vectorScale(state);
        return getPropertyFromInteraction(
                state,
                level,
                vector,
                blockPos,
                Direction.UP,
                Vec3.atLowerCornerOf(vector).multiply(1d / scale.getX(), 1d / scale.getY(), 1d / scale.getZ())
        );
    }

    @Override
    default InteractionResult toggleCT(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && player.getItemInHand(hand).equals(ItemStack.EMPTY)) {
            if (!canToggleCT(state, level, pos))
                return InteractionResult.PASS;
            String property = getPropertyFromInteraction(state, level, pos, hit, true);
            IMultiStateCopycatBlockEntity be = getCopycatBlockEntity(level, pos);
            if (be == null)
                return InteractionResult.PASS;
            be.setEnableCT(property, !be.getMaterialItemStorage().getMaterialItem(property).enableCT());
            be.redraw();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        IMultiStateCopycatBlockEntity copycatBE = getCopycatBlockEntity(context.getLevel(), context.getClickedPos());
        if (copycatBE == null)
            return InteractionResult.PASS;

        String property = getPropertyFromInteraction(
                state,
                context.getLevel(),
                context.getClickedPos(),
                context.getClickLocation(),
                context.getClickedFace(),
                true
        );
        if (!partExists(state, property))
            return InteractionResult.PASS;

        MaterialItemStorage.MaterialItem material = copycatBE.getMaterialItemStorage().getMaterialItem(property);
        ItemStack consumedItem = material.consumedItem();

        if (!copycatBE.getMaterialItemStorage().hasCustomMaterial(property))
            return InteractionResult.PASS;

        Player player = context.getPlayer();
        if (!player.isCreative())
            player.getInventory()
                    .placeItemBackInInventory(consumedItem);
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(material.material()));

        copycatBE.setMaterial(property, AllBlocks.COPYCAT_BASE.getDefaultState());
        copycatBE.setConsumedItem(property, ItemStack.EMPTY);
        return InteractionResult.SUCCESS;
    }

    @Override
    default InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // prioritize wrench interactions over others
        if (player.getItemInHand(hand).is(AllTags.AllItemTags.WRENCH.tag)) {
            InteractionResult result = AllItems.WRENCH.get().useOn(new UseOnContext(player, hand, hit));
            if (result.consumesAction())
                return result;
        }

        InteractionResult result = toggleCT(state, level, pos, player, hand, hit);
        if (result.consumesAction())
            return result;

        if (player == null || !player.mayBuild())
            return InteractionResult.PASS;

        Direction face = hit.getDirection();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState material = getAcceptedBlockState(level, pos, itemInHand, face);

        if (material != null)
            material = prepareMaterial(level, pos, state, player, hand, hit, material);
        if (material == null)
            return InteractionResult.PASS;

        IMultiStateCopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
        if (copycatBE == null)
            return InteractionResult.PASS;
        String property = getPropertyFromInteraction(state, level, pos, hit, true);
        if (!partExists(state, property)) return InteractionResult.PASS;
        if (copycatBE.getMaterialItemStorage().getMaterialItem(property).material()
                .is(material.getBlock())) {
            if (!copycatBE.cycleMaterial(property))
                return InteractionResult.PASS;
            copycatBE.getLevel()
                    .playSound(null, copycatBE.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .75f,
                            .95f);
            return InteractionResult.SUCCESS;
        }
        if (copycatBE.getMaterialItemStorage().hasCustomMaterial(property))
            return InteractionResult.PASS;
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        copycatBE.setMaterial(property, material);
        copycatBE.setConsumedItem(property, itemInHand);
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

    @Override
    default void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer == null)
            return;
        ItemStack offhandItem = placer.getItemInHand(InteractionHand.OFF_HAND);
        BlockState appliedState =
                getAcceptedBlockState(level, pos, offhandItem, Direction.orderedByNearest(placer)[0]);

        if (appliedState == null)
            return;
        IMultiStateCopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
        if (copycatBE == null)
            return;
        for (String property : this.storageProperties()) {
            if (!partExists(state, property))
                continue;
            if (copycatBE.getMaterialItemStorage().hasCustomMaterial(property))
                continue;

            copycatBE.setMaterial(property, appliedState);
            copycatBE.setConsumedItem(property, offhandItem);

            if (placer instanceof Player player && player.isCreative())
                continue;
            offhandItem.shrink(1);
            if (offhandItem.isEmpty()) {
                placer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                break;
            }
        }
    }

    @Override
    default void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.hasBlockEntity() || state.getBlock() == newState.getBlock())
            return;
        if (!isMoving) {
            IMultiStateCopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
            if (copycatBE != null)
                copycatBE.getMaterialItemStorage().getAllConsumedItems().forEach(stack -> Block.popResource(level, pos, stack));
        }
        level.removeBlockEntity(pos);
    }

    @Override
    default void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (player.isCreative()) {
            IMultiStateCopycatBlockEntity copycatBE = getCopycatBlockEntity(level, pos);
            if (copycatBE != null)
                copycatBE.getMaterialItemStorage().getAllProperties().forEach(key -> copycatBE.getMaterialItemStorage().getMaterialItem(key).setConsumedItem(ItemStack.EMPTY));
        }
    }

    @Nullable
    static VoxelShape blockShapeOverride(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pLevel instanceof ScaledBlockAndTintGetter) {
            return Shapes.block(); // todo: patch the blocking check to consider multistates properly
        }
        return null;
    }

    static BlockState getAppearance(IMultiStateCopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {
        String property;
        if (level instanceof ScaledBlockAndTintGetter scaledLevel) {
            property = scaledLevel.getPropertyForRender(state, pos);
        } else {
            property = block.defaultProperty();
        }
        IMultiStateCopycatBlockEntity be = block.getCopycatBlockEntity(level, queryPos);
        if (block.isIgnoredConnectivitySide(property, level, state, side, pos, queryPos))
            return state;

        BlockState material = IMultiStateCopycatBlock.getMaterial(level, pos, property);
        return material.is(Blocks.AIR) ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    static BlockState getMaterial(BlockGetter reader, BlockPos targetPos, String property) {
        if (reader.getBlockEntity(targetPos) instanceof IMultiStateCopycatBlockEntity cbe)
            if (cbe.getMaterialItemStorage().getMaterialItem(property) != null)
                return cbe.getMaterialItemStorage().getMaterialItem(property).material();
        return Blocks.AIR.defaultBlockState();
    }

    /**
     * Whether a part of this copycat can occlude faces of adjacent parts if their shape is fully covered by the copycat.
     *
     * @param property The property corresponding to a part of this copycat.
     * @param level    The world.
     * @param state    The state of the copycat block.
     * @param pos      The position of the copycat block.
     * @return Whether the copycat can occlude faces of adjacent blocks.
     */
    default boolean canOcclude(String property, BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = getMaterial(level, pos, property);
        if (AllBlocks.COPYCAT_BASE.has(material)) return false; // copycat_base is incorrectly set to occlude
        return material.canOcclude();
    }

    void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform);

    @Override
    default boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                              BlockPos fromPos, BlockPos toPos) {
        return isIgnoredConnectivitySide(defaultProperty(), reader, state, face, fromPos, toPos);
    }

    @Override
    default boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                             BlockState state) {
        return canConnectTexturesToward(defaultProperty(), reader, fromPos, toPos, state);
    }

    @Override
    default boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        return false;
    }

    /**
     * Determines whether textures on an adjacent part/block should appear connected to the copycat block part.
     *
     * @param reader  The world which is scaled by {@link IMultiStateCopycatBlock#vectorScale}.
     * @param state   The state of the copycat block part.
     * @param face    The face of the adjacent block/part that is being rendered.
     * @param fromPos The position of the copycat block part.
     * @param toPos   The position of the adjacent block/part.
     * @return Whether the adjacent block/part is not allowed to connect.
     */
    boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face,
                                      BlockPos fromPos, BlockPos toPos);

    /**
     * Determines whether the copycat block part can connect its textures towards the adjacent block/part.
     *
     * @param reader  The world which is scaled by {@link IMultiStateCopycatBlock#vectorScale}.
     * @param fromPos The position of the copycat block part.
     * @param toPos   The position of the adjacent block/part.
     * @param state   The state of the copycat block part.
     * @return Whether the copycat block part can connect its textures towards the adjacent block/part.
     */
    boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                     BlockState state);


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
                return GrassColor.get(0.5D, 1.0D);

            String renderingProperty = MultiStateRenderManager.getRenderingProperty();
            if (renderingProperty != null) {
                return Minecraft.getInstance()
                        .getBlockColors()
                        .getColor(getMaterial(pLevel, pPos, renderingProperty), pLevel, pPos, pTintIndex);
            }

            //First tint from first material
            int tint = Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(ICopycatBlock.getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex);
            BlockEntity be = pLevel.getBlockEntity(pPos);
            if (tint == -1) {
                if (be instanceof IMultiStateCopycatBlockEntity mscb) {
                    for (String property : mscb.getMaterialItemStorage().getAllProperties()) {
                        int anyTint = Minecraft.getInstance()
                                .getBlockColors()
                                .getColor(getMaterial(pLevel, pPos, property), pLevel, pPos, pTintIndex);
                        if (anyTint != -1)
                            return anyTint;
                    }
                }
            }
            return tint;
        }
    }
}
