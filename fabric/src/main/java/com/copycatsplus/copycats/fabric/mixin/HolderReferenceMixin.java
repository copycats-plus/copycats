package com.copycatsplus.copycats.fabric.mixin;

import com.copycatsplus.copycats.fabric.mixin_interfaces.HolderReferenceAccessor;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin implements HolderReferenceAccessor {

    @Shadow
    abstract <T> void bind(ResourceKey<T> key, T value);

    @Shadow
    public abstract <T> ResourceKey<T> key();

    @Override
    public <T> void copycats$bindValue(T value) {
        this.bind(this.key(), value);
    }
}
