package com.copycatsplus.copycats.forge.mixin.compat.registrate;

import com.copycatsplus.copycats.forge.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreateRegistrate.class)
public class CreateRegistrateMixin implements CreateRegistrateAccessor {
    @Shadow protected RegistryObject<CreativeModeTab> currentTab;

    @Override
    public void copycats$setCreativeTab(RegistryObject<CreativeModeTab> tab) {
        currentTab = tab;
    }
}
