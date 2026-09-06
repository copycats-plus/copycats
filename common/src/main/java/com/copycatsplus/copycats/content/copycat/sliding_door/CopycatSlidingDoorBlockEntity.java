package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.mixin.copycat.sliding_door.SlidingDoorBlockEntityAccessor;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatSlidingDoorBlockEntity extends SlidingDoorBlockEntity implements ICopycatBlockEntity {

    protected BlockState material;
    protected ItemStack lightItem;
    protected ItemStack consumedItem;
    protected boolean enableCT;

    public CopycatSlidingDoorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        ICopycatBlockEntity.super.init();
    }

    public LerpedFloat animation() {
        return ((SlidingDoorBlockEntityAccessor) this).getAnimation();
    }

    @Override
    public boolean shouldRenderSpecial(BlockState state) {
        return super.shouldRenderSpecial(state);
    }

    @Override
    public BlockState getMaterial() {
        return material;
    }

    @Override
    public ItemStack getLightItem() {
        return lightItem;
    }

    @Override
    public ItemStack getConsumedItem() {
        return consumedItem;
    }

    @Override
    public boolean isCTEnabled() {
        return enableCT;
    }

    @Override
    public void setMaterialInternal(BlockState material) {
        this.material = material;
    }

    @Override
    public void setLightItemInternal(ItemStack stack) {
        this.lightItem = stack;
    }

    @Override
    public void setConsumedItemInternal(ItemStack consumedItem) {
        this.consumedItem = consumedItem;
    }

    @Override
    public void setCTEnabledInternal(boolean value) {
        enableCT = value;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        ICopycatBlockEntity.super.onLoad();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return ICopycatBlockEntity.super.getRequiredItems(state).union(super.getRequiredItems(state));
    }

    @Override
    public void transform(BlockEntity blockEntity, StructureTransform transform) {
        ICopycatBlockEntity.super.transform(blockEntity, transform);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        ICopycatBlockEntity.read(this, tag, registries, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeSafe(tag, registries);
        ICopycatBlockEntity.writeSafe(this, tag, registries);
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        ICopycatBlockEntity.write(this, tag, registries, clientPacket);
    }
}

