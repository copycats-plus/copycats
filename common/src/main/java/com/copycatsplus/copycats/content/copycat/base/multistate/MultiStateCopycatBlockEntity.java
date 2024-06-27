package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import com.simibubi.create.foundation.utility.Iterate;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class MultiStateCopycatBlockEntity extends SmartBlockEntity implements
        IFunctionalCopycatBlockEntity, ISpecialBlockEntityItemRequirement, ITransformableBlockEntity, IPartialSafeNBT {

    private final MaterialItemStorage materialItemStorage;

    public MultiStateCopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            materialItemStorage = MaterialItemStorage.create(mscb.maxMaterials(), mscb.storageProperties());
        } else {
            materialItemStorage = MaterialItemStorage.create(1, Set.of("block"));
        }
    }

    @Override
    public CopycatBlockEntity getCopycatBlockEntity() {
        return null;
    }

    @Override
    public void setLevel(@NotNull Level level) {
        super.setLevel(level);
    }

    public void updateTransform() {
        BlockStateTransform transform = getBlockState().getValue(MultiStateCopycatBlock.TRANSFORM);
        if (transform != BlockStateTransform.ABCD) {
            MultiStateCopycatBlock block = (MultiStateCopycatBlock) getBlockState().getBlock();
            transform.undoTransform(
                    r -> block.rotate(getBlockState(), this, r),
                    m -> block.mirror(getBlockState(), this, m)
            );
        }
        getLevel().setBlock(getBlockPos(), getBlockState().setValue(MultiStateCopycatBlock.TRANSFORM, BlockStateTransform.ABCD), 2 | 4 | 16 | 32);
    }

    public boolean cycleMaterial(String property) {
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

    public MaterialItemStorage getMaterialItemStorage() {
        return materialItemStorage;
    }

    public void setMaterial(String property, BlockState blockState) {
        BlockState wrapperState = getBlockState();

        BlockState finalMaterial = blockState;
        if (!getMaterialItemStorage().getMaterialItem(property).material().is(finalMaterial.getBlock()))
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = worldPosition.relative(side);
                BlockState neighbourState = level.getBlockState(neighbour);
                if (neighbourState != wrapperState)
                    continue;
                if (!(level.getBlockEntity(neighbour) instanceof MultiStateCopycatBlockEntity cbe))
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
        if (!level.isClientSide()) {
            notifyUpdate();
            return;
        }
        redraw();
    }

    public void setConsumedItem(String property, ItemStack itemStack) {
        getMaterialItemStorage().getMaterialItem(property).setConsumedItem(itemStack);
        setChanged();
    }

    public void setEnableCT(String property, boolean value) {
        getMaterialItemStorage().getMaterialItem(property).setEnableCT(value);
        notifyUpdate();
    }

    public void redraw() {
        if (!isVirtual())
            requestModelUpdate();
        if (hasLevel()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(getBlockPos());
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        List<ItemStack> stacks = getMaterialItemStorage().getAllConsumedItems();
        if (stacks.isEmpty())
            return ItemRequirement.NONE;
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stacks);
    }

    @Override
    public void transform(StructureTransform transform) {
        // TODO: probably need additional logic
        for (String key : getMaterialItemStorage().getAllProperties()) {
            getMaterialItemStorage().getMaterialItem(key).setMaterial(transform.apply(getMaterialItemStorage().getMaterialItem(key).material()));
        }
        notifyUpdate();
    }

    public abstract void requestModelUpdate();

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);

        tag.put("material_data", materialItemStorage.serializeSafe());
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);

        tag.put("material_data", materialItemStorage.serialize());
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            boolean anyUpdated = materialItemStorage.deserialize(tag.getCompound("material_data"));

            if (clientPacket && anyUpdated)
                redraw();
        }
    }

    public void migrateData(CopycatBlockEntity copycatBlockEntity) {
        if (getBlockState().getBlock() instanceof MultiStateCopycatBlock mscb) {
            ResourceLocation blockId = copycatBlockEntity.getBlockState().getBlock().builtInRegistryHolder().key().location();
            Copycats.LOGGER.debug("Converting block({}) at @{} to a multistate copycat", blockId.toString(), copycatBlockEntity.getBlockPos().toShortString());
            //Set the first property available to have the item and mat.
            MaterialItemStorage.MaterialItem materialItem = materialItemStorage.getMaterialItem(getMaterialItemStorage().getAllProperties().stream().filter(prop -> mscb.partExists(getBlockState(), prop)).findFirst().get());
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
            redraw();
        }
    }

    @ExpectPlatform
    @NotNull
    public static MultiStateCopycatBlockEntity create(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return null;
    }
}
