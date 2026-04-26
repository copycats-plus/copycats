package com.copycatsplus.copycats.foundation.copycat;

import java.util.List;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Base class for simple copycat block entities. Extend this class for simple copycat block entities that do not require
 * another base class.
 * <p>
 * Note: DO NOT check for simple copycats with instanceof checks against this class. Copycats may implement
 * {@link ICopycatBlockEntity} without extending this class. Check for simple copycats with instanceof checks
 * against {@link ICopycatBlockEntity} instead.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CCCopycatBlockEntity extends SmartBlockEntity implements ICopycatBlockEntity {

    protected BlockState material;
    protected ItemStack consumedItem;
    protected boolean enableCT;

    public CCCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        ICopycatBlockEntity.super.init();
    }

    public Level getLevel() {
        return super.getLevel();
    }

    public BlockPos getBlockPos() {
        return super.getBlockPos();
    }

    public BlockState getBlockState() {
        return super.getBlockState();
    }

    public HolderGetter<Block> blockHolderGetter() {
        return super.blockHolderGetter();
    }

    public void setBlockState(BlockState blockState) {
        super.setBlockState(blockState);
    }

    public void setLevel(Level level) {
        super.setLevel(level);
    }

    @Override
    public BlockState getMaterial() {
        return material;
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
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return ICopycatBlockEntity.super.getRequiredItems(state);
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

