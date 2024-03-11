package com.copycatsplus.copycats.fabric.mixin.compat.registrate;

import com.copycatsplus.copycats.fabric.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreateRegistrate.class)
public class CreateRegistrateMixin implements CreateRegistrateAccessor {
    @Shadow protected ResourceKey<CreativeModeTab> currentTab;

    @Override
    public void copycats$setCreativeTab(ResourceKey<CreativeModeTab> tab) {
        currentTab = tab;
    }
}
