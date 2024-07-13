package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CopycatRegistrate;
import com.copycatsplus.copycats.forge.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

public class CopycatRegistrateImpl extends CopycatRegistrate {

    protected CopycatRegistrateImpl(String modid) {
        super(modid);
    }


    public static <Tab> CreateRegistrate setTab(Tab tab) {
        ((CreateRegistrateAccessor) getInstance()).copycats$setCreativeTab((RegistryObject<CreativeModeTab>) tab);
        return getInstance();
    }
}
