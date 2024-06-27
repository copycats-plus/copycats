package com.copycatsplus.copycats.content.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.mixin.copycat.base.CopycatBlockEntityAccessor;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IFunctionalCopycatBlockEntity extends CTCopycatBlockEntity, ISpecialBlockEntityItemRequirement, ITransformableBlockEntity, IPartialSafeNBT {
    CopycatBlockEntity getCopycatBlockEntity();

    default Level getLevel() {
        return getCopycatBlockEntity().getLevel();
    }

    default BlockPos getBlockPos() {
        return getCopycatBlockEntity().getBlockPos();
    }

    default BlockState getBlockState() {
        return getCopycatBlockEntity().getBlockState();
    }

    default void setLevel(Level level) {
        getCopycatBlockEntity().setLevel(level);
    }

    default BlockState getMaterial() {
        return getCopycatBlockEntity().getMaterial();
    }

    default boolean hasCustomMaterial() {
        return getCopycatBlockEntity().hasCustomMaterial();
    }

    default void setMaterial(BlockState material) {
        getCopycatBlockEntity().setMaterial(material);
    }

    default boolean cycleMaterial() {
        return getCopycatBlockEntity().cycleMaterial();
    }

    default ItemStack getConsumedItem() {
        return getCopycatBlockEntity().getConsumedItem();
    }

    default void setConsumedItem(ItemStack consumedItem) {
        getCopycatBlockEntity().setConsumedItem(consumedItem);
    }

    @Override
    default ItemRequirement getRequiredItems(BlockState state) {
        return getCopycatBlockEntity().getRequiredItems(state);
    }

    @Override
    default void transform(StructureTransform transform) {
        getCopycatBlockEntity().transform(transform);
    }

    default void read(CompoundTag compound, boolean clientPacket) {
        ((CopycatBlockEntityAccessor) getCopycatBlockEntity()).callRead(compound, clientPacket);
    }

    default void writeSafe(CompoundTag tag) {
        getCopycatBlockEntity().writeSafe(tag);
    }

    default void write(CompoundTag compound, boolean clientPacket) {
        ((CopycatBlockEntityAccessor) getCopycatBlockEntity()).callWrite(compound, clientPacket);
    }

    @Override
    default boolean isCTEnabled() {
        return ((CTCopycatBlockEntity) getCopycatBlockEntity()).isCTEnabled();
    }

    @Override
    default void setCTEnabled(boolean value) {
        ((CTCopycatBlockEntity) getCopycatBlockEntity()).setCTEnabled(value);
    }

    @Override
    default void callRedraw() {
        ((CTCopycatBlockEntity) getCopycatBlockEntity()).callRedraw();
    }
}
