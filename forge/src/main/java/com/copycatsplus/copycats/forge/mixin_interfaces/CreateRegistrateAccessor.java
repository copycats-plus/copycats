package com.copycatsplus.copycats.forge.mixin_interfaces;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public interface CreateRegistrateAccessor {

    void copycats$setCreativeTab(RegistryObject<CreativeModeTab> tab);
}
