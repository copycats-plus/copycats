package com.copycatsplus.copycats.mixin.entity;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface HolderReferenceAccessor {

    @Invoker
    void callBind(ResourceKey<?> key, Object value);
}
