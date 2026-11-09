package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatCogWheelBlockEntity extends BracketedKineticBlockEntity implements IMultiStateCopycatBlockEntity {
    protected ItemStack lightItem;
    private MaterialItemStorage storage;

    public CopycatCogWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        init();
    }

    @Override
    public MaterialItemStorage getMaterialItemStorage() {
        return storage;
    }

    @Override
    public void setMaterialItemStorageInternal(MaterialItemStorage storage) {
        this.storage = storage;
    }

    @Override
    public ItemStack getLightItem() {
        return lightItem;
    }

    @Override
    public void setLightItemInternal(ItemStack stack) {
        this.lightItem = stack;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        IMultiStateCopycatBlockEntity.super.onLoad();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return IMultiStateCopycatBlockEntity.super.getRequiredItems(state).union(super.getRequiredItems(state));
    }

    @Override
    public void transform(BlockEntity blockEntity, StructureTransform transform) {
        super.transform(blockEntity, transform);
        IMultiStateCopycatBlockEntity.super.transform(blockEntity, transform);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        IMultiStateCopycatBlockEntity.read(this, tag, registries, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag, HolderLookup.Provider registries) {
        super.writeSafe(tag, registries);
        IMultiStateCopycatBlockEntity.writeSafe(this, tag, registries);
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        IMultiStateCopycatBlockEntity.write(this, tag, registries, clientPacket);
    }
}
