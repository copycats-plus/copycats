package com.copycatsplus.copycats.mixin.minecraft;

import com.copycatsplus.copycats.mixin_interfaces.CreativeTabExpander;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeModeTab.class)
public class CreativeModTabMixin implements CreativeTabExpander {
    @Mutable
    @Shadow @Final public static CreativeModeTab[] TABS;

    @Override
    public int copycats$expandTabCount() {
        CreativeModeTab[] tempGroups = TABS;
        TABS = new CreativeModeTab[TABS.length + 1];
        System.arraycopy(tempGroups, 0, TABS, 0, tempGroups.length);
        return TABS.length - 1;
    }
}
