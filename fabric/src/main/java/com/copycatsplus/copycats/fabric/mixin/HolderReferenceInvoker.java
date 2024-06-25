package com.copycatsplus.copycats.fabric.mixin;

import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface HolderReferenceInvoker {
    @Invoker
    <T> void callBindValue(T value);
}
