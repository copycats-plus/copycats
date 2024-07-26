package com.copycatsplus.copycats.forge.mixin.foundation.copycat;

import net.minecraftforge.client.model.data.ModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(net.minecraftforge.client.model.data.ModelDataMap.class)
public interface ModelDataMapAccessor {
    @Accessor
    Map<ModelProperty<?>, Object> getBackingMap();
}
