package com.copycatsplus.copycats.fabric.mixin;

import com.copycatsplus.copycats.fabric.mixin_interfaces.HolderReferenceAccessor;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Allow a holder reference to be bound to a value. Used for custom registration.
 */
@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin implements HolderReferenceAccessor {

    @Shadow
    abstract <T> void bindValue(T value);

    @Override
    public <T> void copycats$bindValue(T value) {
        this.bindValue(value);
    }
}
