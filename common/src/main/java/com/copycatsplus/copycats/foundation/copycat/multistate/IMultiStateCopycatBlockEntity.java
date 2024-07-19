package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

/**
 * An interface with implementation for all multi-state copycat block entities.
 * <p>
 * Implementors should create a field to store the materials and redirect calls of
 * {@link IMultiStateCopycatBlockEntity#read},
 * {@link IMultiStateCopycatBlockEntity#writeSafe} and
 * {@link IMultiStateCopycatBlockEntity#write} to this interface.
 * <p>
 * If the concrete class is not a subclass of {@link MultiStateCopycatBlockEntity},
 * it should also be registered in platform-specific MultiStateCopycatBlockEntityMixins as a mixin target.
 * <p>
 * It is not recommended to override undocumented methods in this interface, since they are considered internal to
 * the implementation of copycats.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IMultiStateCopycatBlockEntity extends ICopycatBlockEntity {
    MaterialItemStorage getMaterialItemStorage();

    @ApiStatus.OverrideOnly
    void setMaterialItemStorageInternal(MaterialItemStorage storage);

    @Override
    default void init() {
        if (getBlockState().getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            Set<String> properties = copycatBlock.storageProperties();
            setMaterialItemStorageInternal(MaterialItemStorage.create(properties));
        } else {
            setMaterialItemStorageInternal(MaterialItemStorage.create(Set.of("block")));
        }
    }

    @Override
    default BlockState getMaterial() {
        return getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).material();
    }

    @Override
    default ItemStack getConsumedItem() {
        return getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).consumedItem();
    }

    @Override
    default boolean isCTEnabled() {
        return getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).enableCT();
    }

    @Override
    default void setMaterialInternal(BlockState material) {
        getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).setMaterial(material);
    }

    @Override
    default void setConsumedItemInternal(ItemStack consumedItem) {
        getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).setConsumedItem(consumedItem);
    }

    @Override
    default void setCTEnabledInternal(boolean value) {
        getMaterialItemStorage().getMaterialItem(getBlock().defaultProperty()).setEnableCT(value);
    }

    @Override
    default IMultiStateCopycatBlock getBlock() {
        return (IMultiStateCopycatBlock) getBlockState().getBlock();
    }

    @Override
    default boolean hasCustomMaterial() {
        return !getMaterialItemStorage().getAllMaterials().stream().allMatch(state -> state.is(AllBlocks.COPYCAT_BASE.get()));
    }

    default void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = getBlockState();

        BlockState finalMaterial = blockState;
        if (!getMaterialItemStorage().getMaterialItem(property).material().is(finalMaterial.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = getBlockPos().relative(side);
                BlockState neighbourState = getLevel().getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(getLevel().getBlockEntity(neighbour) instanceof IMultiStateCopycatBlockEntity cbe))
                    continue;
                BlockState otherMaterial = cbe.getMaterialItemStorage().getMaterialItem(property).material();
                if (!otherMaterial.is(blockState.getBlock()))
                    continue;
                blockState = otherMaterial;
                break;
            }
        MaterialItemStorage.MaterialItem materialItem = getMaterialItemStorage().getMaterialItem(property);
        materialItem.setMaterial(blockState);
        getMaterialItemStorage().storeMaterialItem(property, materialItem);
        if (!getLevel().isClientSide()) {
            notifyUpdate();
            return;
        }
        BlockEntityUtils.redraw((BlockEntity) this);
    }

    default boolean cycleMaterial(String property) {
        BlockState material = getMaterialItemStorage().getMaterialItem(property).material();
        if (material.hasProperty(TrapDoorBlock.HALF) && material.getOptionalValue(TrapDoorBlock.OPEN)
                .orElse(false))
            setMaterial(property, material.cycle(TrapDoorBlock.HALF));
        else if (material.hasProperty(BlockStateProperties.FACING))
            setMaterial(property, material.cycle(BlockStateProperties.FACING));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            setMaterial(property, material.setValue(BlockStateProperties.HORIZONTAL_FACING,
                    material.getValue(BlockStateProperties.HORIZONTAL_FACING)
                            .getClockWise()));
        else if (material.hasProperty(BlockStateProperties.AXIS))
            setMaterial(property, material.cycle(BlockStateProperties.AXIS));
        else if (material.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
            setMaterial(property, material.cycle(BlockStateProperties.HORIZONTAL_AXIS));
        else if (material.hasProperty(BlockStateProperties.LIT))
            setMaterial(property, material.cycle(BlockStateProperties.LIT));
        else if (material.hasProperty(RoseQuartzLampBlock.POWERING))
            setMaterial(property, material.cycle(RoseQuartzLampBlock.POWERING));
        else
            return false;

        return true;
    }

    default void setConsumedItem(String property, ItemStack itemStack) {
        getMaterialItemStorage().getMaterialItem(property).setConsumedItem(itemStack);
        notifyUpdate();
    }

    default void setEnableCT(String property, boolean value) {
        getMaterialItemStorage().getMaterialItem(property).setEnableCT(value);
        notifyUpdate();
    }

    @Override
    default void transform(StructureTransform transform) {
        getBlock().transformStorage(this.getBlockState(), this, transform);
        for (String key : getMaterialItemStorage().getAllProperties()) {
            getMaterialItemStorage().getMaterialItem(key).setMaterial(transform.apply(getMaterialItemStorage().getMaterialItem(key).material()));
        }
        notifyUpdate();
    }

    static void read(IMultiStateCopycatBlockEntity self, CompoundTag tag, boolean clientPacket) {
        if (self.getBlockState().getBlock() instanceof IMultiStateCopycatBlock) {
            boolean anyUpdated = self.getMaterialItemStorage().deserialize(tag.getCompound("material_data"));

            if (clientPacket && anyUpdated)
                BlockEntityUtils.redraw((BlockEntity) self); // not calling self.redraw() because Extended Cogwheels overwrites it to be protected
        }
    }

    static void writeSafe(IMultiStateCopycatBlockEntity self, CompoundTag tag) {
        tag.put("material_data", self.getMaterialItemStorage().serializeSafe());
    }

    static void write(IMultiStateCopycatBlockEntity self, CompoundTag tag, boolean clientPacket) {
        tag.put("material_data", self.getMaterialItemStorage().serialize());
    }
}
