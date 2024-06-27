package com.copycatsplus.copycats.mixin.copycat.base;

import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CopycatBlockEntity.class)
public interface CopycatBlockEntityAccessor {
    @Invoker
    void callRead(CompoundTag tag, boolean clientPacket);

    @Invoker
    void callWrite(CompoundTag tag, boolean clientPacket);
}
