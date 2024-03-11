package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CopycatRegistrate;
import com.copycatsplus.copycats.fabric.mixin_interfaces.CreateRegistrateAccessor;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class CopycatRegistrateImpl extends CopycatRegistrate {

    protected CopycatRegistrateImpl(String modid) {
        super(modid);
    }


    public static <Tab> CreateRegistrate setTab(Tab tab) {
        ((CreateRegistrateAccessor) getInstance()).copycats$setCreativeTab((ResourceKey<CreativeModeTab>) tab);
        return getInstance();
    }
}
