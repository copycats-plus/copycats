package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin implements IFunctionalCopycatBlockEntity {
    @Override
    public CopycatBlockEntity getCopycatBlockEntity() {
        return (CopycatBlockEntity) (Object) this;
    }
}
