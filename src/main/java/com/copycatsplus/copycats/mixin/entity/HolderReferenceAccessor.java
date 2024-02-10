package com.copycatsplus.copycats.mixin.entity;

import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface HolderReferenceAccessor {
    @Invoker
    void callBindValue(Object value);
}
