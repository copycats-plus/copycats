package com.copycatsplus.copycats.mixin.featuretoggle;

import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreativeModeTab.class)
public interface CreativeModeTabsAccessor {

    //Doesnt exist in 1.19.2
/*    @Invoker
    static void callBuildAllTabContents(CreativeModeTab.ItemDisplayParameters displayParameters) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static CreativeModeTab.ItemDisplayParameters getCACHED_PARAMETERS() {
        throw new UnsupportedOperationException();
    }*/
}
