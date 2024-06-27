package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CopycatShaftBlockEntity extends BracketedKineticBlockEntity implements IFunctionalCopycatBlockEntity {

    protected CopycatBlockEntity copycatBlockEntity;

    public CopycatShaftBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        copycatBlockEntity = new CopycatBlockEntity(CCBlockEntityTypes.COPYCAT.get(), pos, state);
    }

    @Override
    public CopycatBlockEntity getCopycatBlockEntity() {
        return copycatBlockEntity;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        IFunctionalCopycatBlockEntity.super.setLevel(level);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        IFunctionalCopycatBlockEntity.super.read(compound, clientPacket);
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        IFunctionalCopycatBlockEntity.super.writeSafe(tag);
        super.writeSafe(tag);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        IFunctionalCopycatBlockEntity.super.write(compound, clientPacket);
    }

    @Override
    public void callRedraw() {
        IFunctionalCopycatBlockEntity.super.callRedraw();
    }
}
