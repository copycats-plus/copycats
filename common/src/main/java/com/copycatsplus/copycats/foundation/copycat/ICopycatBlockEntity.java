package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.copycatsplus.copycats.utility.ItemUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.api.schematic.nbt.PartialSafeNBT;
import com.simibubi.create.api.schematic.requirement.SpecialBlockEntityItemRequirement;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.IMergeableBE;
import net.createmod.catnip.data.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An interface with implementation for all simple copycat block entities.
 * <p>
 * Implementors should create a field to store the material, consumed item and CT toggle, and redirect calls of
 * {@link ICopycatBlockEntity#onLoad},
 * {@link ICopycatBlockEntity#read},
 * {@link ICopycatBlockEntity#writeSafe} and
 * {@link ICopycatBlockEntity#write} to this interface.
 * <p>
 * If the concrete class is not a subclass of {@link CCCopycatBlockEntity},
 * it should also be registered in platform-specific CopycatBlockEntityMixins as a mixin target.
 * <p>
 * It is not recommended to override undocumented methods in this interface, since they are considered internal to
 * the implementation of copycats.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICopycatBlockEntity extends SpecialBlockEntityItemRequirement, TransformableBlockEntity, PartialSafeNBT, IMergeableBE {

    void notifyUpdate();

    Level getLevel();

    BlockPos getBlockPos();

    BlockState getBlockState();

    HolderGetter<Block> blockHolderGetter();

    void setBlockState(BlockState blockState);

    void setLevel(Level level);

    BlockState getMaterial();

    ItemStack getLightItem();

    ItemStack getConsumedItem();

    boolean isCTEnabled();

    @ApiStatus.OverrideOnly
    void setMaterialInternal(BlockState material);

    @ApiStatus.OverrideOnly
    void setLightItemInternal(ItemStack stack);

    @ApiStatus.OverrideOnly
    void setConsumedItemInternal(ItemStack consumedItem);

    @ApiStatus.OverrideOnly
    void setCTEnabledInternal(boolean value);

    /**
     * Implementors should call this method in their constructor.
     */
    default void init() {
        setMaterialInternal(AllBlocks.COPYCAT_BASE.getDefaultState());
        setConsumedItemInternal(ItemStack.EMPTY);
        setLightItem(ItemStack.EMPTY);
        setCTEnabledInternal(true);
    }

    default void onLoad() {
        BlockEntityUtils.updateLight((BlockEntity) this);
    }

    default ICopycatBlock getBlock() {
        Block block = getBlockState().getBlock();
        if (block instanceof ICopycatBlock copycatBlock)
            return copycatBlock;
        // the block state might not be a copycat block in some virtual worlds
        // return sensible defaults in those cases
        return new ICopycatBlock() {
        };
    }

    default boolean hasCustomMaterial() {
        return !AllBlocks.COPYCAT_BASE.has(getMaterial());
    }

    default void setMaterial(BlockState blockState) {
        BlockState wrapperState = getBlockState();

        if (!getMaterial().is(blockState.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = getBlockPos().relative(side);
                BlockState neighbourState = getLevel().getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(getLevel().getBlockEntity(neighbour) instanceof ICopycatBlockEntity cbe))
                    continue;
                BlockState otherMaterial = cbe.getMaterial();
                if (!otherMaterial.is(blockState.getBlock()))
                    continue;
                blockState = otherMaterial;
                break;
            }

        setMaterialInternal(blockState);

        BlockEntityUtils.redraw((BlockEntity) this);
    }

    default boolean cycleMaterial() {
        BlockState material = getMaterial();
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN)
                .orElse(false))
            setMaterial(material.cycle(TrapDoorBlock.HALF));
        else if (material.hasProperty(BlockStateProperties.FACING))
            setMaterial(material.cycle(BlockStateProperties.FACING));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            setMaterial(material.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    material.getValue(BlockStateProperties.HORIZONTAL_FACING)
                            .getClockWise()));
        else if (material.hasProperty(BlockStateProperties.AXIS))
            setMaterial(material.cycle(BlockStateProperties.AXIS));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
            setMaterial(material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        else if (material.hasProperty(BlockStateProperties.LIT))
            setMaterial(material.cycle(BlockStateProperties.LIT));
        else if (material.hasProperty(RoseQuartzLampBlock.POWERING))
            setMaterial(material.cycle(RoseQuartzLampBlock.POWERING));
        else
            return false;

        return true;
    }

    default int getLightLevel() {
        ItemStack lightItem = getLightItem();

        if(lightItem.is(Items.GLOWSTONE)) return 15;
        if(lightItem.is(Items.TORCH)) return 14;
        if(lightItem.is(Items.SOUL_TORCH)) return 7;
        return -1;
    }

    default void setLightItem(ItemStack stack) {
        setLightItemInternal(ItemUtils.copyStackWithSize(stack, 1));

        BlockEntityUtils.redraw((BlockEntity) this);
    }

    default void setConsumedItem(ItemStack stack) {
        setConsumedItemInternal(ItemUtils.copyStackWithSize(stack, 1));
        notifyUpdate();
    }

    default void setCTEnabled(boolean value) {
        setCTEnabledInternal(value);
        notifyUpdate();
    }

    @Override
    default ItemRequirement getRequiredItems(BlockState state) {
        if (getConsumedItem().isEmpty())
            return ItemRequirement.NONE;
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, getConsumedItem());
    }

    @Override
    default void accept(BlockEntity other) {
        if (other instanceof ICopycatBlockEntity be) {
            setMaterial(be.getMaterial());
            setConsumedItem(be.getConsumedItem());
            setCTEnabled(be.isCTEnabled());
            BlockEntityUtils.redraw((BlockEntity) this);
        }
    }

    @Override
    default void transform(BlockEntity blockEntity, StructureTransform transform) {
        setMaterialInternal(transform.apply(getMaterial()));
        notifyUpdate();
    }

    static void read(ICopycatBlockEntity self, CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        if (tag.contains("EnableCT")) // need to check because copycats migrated from C:Connected don't have this tag
            self.setCTEnabled(tag.getBoolean("EnableCT"));
        else
            self.setCTEnabled(true);

        self.setConsumedItem(ItemStack.parseOptional(registries, (tag.getCompound("Item"))));

        self.setLightItem(ItemStack.parseOptional(registries, tag.getCompound("LightItem")));

        BlockState prevMaterial = self.getMaterial();
        if (!tag.contains("Material")) {
            self.setConsumedItem(ItemStack.EMPTY);
            return;
        }

        self.setMaterialInternal(NbtUtils.readBlockState(self.blockHolderGetter(), tag.getCompound("Material")));

        // Validate Material
        if (self.getMaterial() != null && !clientPacket) {
            BlockState blockState = self.getBlockState();
            if (blockState == null)
                return;
            if (!(blockState.getBlock() instanceof ICopycatBlock cb))
                return;
            BlockState acceptedBlockState = cb.getAcceptedBlockState(self.getLevel(), self.getBlockPos(), self.getConsumedItem(), null);
            if (acceptedBlockState != null && self.getMaterial().is(acceptedBlockState.getBlock()))
                return;
            self.setConsumedItem(ItemStack.EMPTY);
            self.setMaterialInternal(AllBlocks.COPYCAT_BASE.getDefaultState());
        }

        if (prevMaterial != self.getMaterial())
            BlockEntityUtils.redraw((BlockEntity) self); // not calling self.redraw() because Extended Cogwheels overwrites it to be protected
    }

    static void writeSafe(ICopycatBlockEntity self, CompoundTag tag, HolderLookup.Provider registries) {
        ItemStack stackWithoutNBT = self.getConsumedItem().copy();
        ItemStack stackWithoutComponents = new ItemStack(stackWithoutNBT.getItemHolder(), stackWithoutNBT.getCount(), DataComponentPatch.EMPTY);
        ItemStack lightItemWithoutNBT = self.getConsumedItem().copy();
        ItemStack lightItemWithoutComponents = new ItemStack(lightItemWithoutNBT.getItemHolder(), lightItemWithoutNBT.getCount(), DataComponentPatch.EMPTY);
        BlockEntityUtils.saveMetadata((BlockEntity) self, tag);
        write(tag, stackWithoutComponents, self.getMaterial(), lightItemWithoutComponents, registries, self.isCTEnabled());
    }

    static void write(ICopycatBlockEntity self, CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        write(tag, self.getConsumedItem(), self.getMaterial(), self.getLightItem(), registries, self.isCTEnabled());
    }

    @ApiStatus.Internal
    static void write(CompoundTag tag, ItemStack stack, BlockState material, ItemStack lightItem, HolderLookup.Provider registries, boolean enableCT) {
        tag.put("Item", ItemUtils.serializeNBT(stack, registries));
        tag.put("Material", NbtUtils.writeBlockState(material));
        tag.put("LightItem", ItemUtils.serializeNBT(lightItem, registries));
        tag.putBoolean("EnableCT", enableCT);
    }
}
