package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Base class for multi-state copycat block entities. Extend this class for multi-state copycat block entities that do not require
 * another base class.
 * <p>
 * Note: DO NOT check for multi-state copycats with instanceof checks against this class. Copycats may implement
 * {@link IMultiStateCopycatBlockEntity} without extending this class. Check for multi-state copycats with instanceof checks
 * against {@link IMultiStateCopycatBlockEntity} instead.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiStateCopycatBlockEntity extends SmartBlockEntity implements IMultiStateCopycatBlockEntity {

    private MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        init();
    }

    public MaterialItemStorage getMaterialItemStorage() {
        return materialItemStorage;
    }

    @Override
    public void setMaterialItemStorageInternal(MaterialItemStorage storage) {
        materialItemStorage = storage;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        IMultiStateCopycatBlockEntity.read(this, tag, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        IMultiStateCopycatBlockEntity.writeSafe(this, tag);
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        IMultiStateCopycatBlockEntity.write(this, tag, clientPacket);
    }

    public void migrateData(ICopycatBlockEntity copycatBlockEntity) {
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            ResourceLocation blockId = copycatBlockEntity.getBlockState().getBlock().builtInRegistryHolder().key().location();
            Copycats.LOGGER.debug("Converting block({}) at @{} to a multistate copycat", blockId.toString(), copycatBlockEntity.getBlockPos().toShortString());
            //Set the first property available to have the item and mat.
            String firstProperty = getMaterialItemStorage().getAllProperties().stream()
                    .filter(prop -> mscb.partExists(getBlockState(), prop))
                    .findFirst()
                    .orElse(null);
            if (firstProperty == null) {
                Copycats.LOGGER.error("Failed to convert block({}) at @{} to a multistate copycat: no valid properties found", blockId.toString(), copycatBlockEntity.getBlockPos().toShortString());
                BlockEntityUtils.redraw(this);
                return;
            }
            MaterialItemStorage.MaterialItem materialItem = materialItemStorage.getMaterialItem(firstProperty);
            materialItem.setMaterial(copycatBlockEntity.getMaterial());
            materialItem.setConsumedItem(copycatBlockEntity.getConsumedItem());

            //Sets only the material so that it looks the same as the old blocks but wont give you free items
            for (String property : mscb.storageProperties()) {
                if (mscb.partExists(getBlockState(), property)) {
                    if (!getMaterialItemStorage().hasCustomMaterial(property)) {
                        MaterialItemStorage.MaterialItem store = materialItemStorage.getMaterialItem(property);
                        store.setMaterial(copycatBlockEntity.getMaterial());
                        store.setConsumedItem(ItemStack.EMPTY);
                    }
                }
            }
            BlockEntityUtils.redraw(this);
        }
    }
}
