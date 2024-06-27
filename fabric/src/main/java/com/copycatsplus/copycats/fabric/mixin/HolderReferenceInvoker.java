package com.copycatsplus.copycats.fabric.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface HolderReferenceInvoker {
    @Invoker
    <T> void callBind(ResourceKey<T> key, T value);
}
