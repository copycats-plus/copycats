package com.copycatsplus.copycats.mixin.featuretoggle;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CreativeModeTabs.class)
public interface CreativeModeTabsAccessor {
    @Invoker
    static void callBuildAllTabContents(CreativeModeTab.ItemDisplayParameters pParameters) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static CreativeModeTab.ItemDisplayParameters getCACHED_PARAMETERS() {
        throw new UnsupportedOperationException();
    }
}
