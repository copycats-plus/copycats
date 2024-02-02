package com.copycatsplus.copycats.compat;

import com.copycatsplus.copycats.CCCreativeTabs;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@JeiPlugin
public class CopycatsJEI implements IModPlugin {
    private static final ResourceLocation ID = Copycats.asResource("jei_plugin");

    public static IIngredientManager MANAGER;

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        MANAGER = jeiRuntime.getIngredientManager();
    }

    /**
     * Add/remove items from JEI at runtime according to what features are enabled
     */
    public static void refreshItemList() {
        if (MANAGER != null) {
            MANAGER.removeIngredientsAtRuntime(
                    VanillaTypes.ITEM_STACK,
                    CCCreativeTabs.ITEMS.stream()
                            .collect(Collectors.toList())
            );
            MANAGER.addIngredientsAtRuntime(
                    VanillaTypes.ITEM_STACK,
                    CCCreativeTabs.ITEMS.stream()
                            .filter(x -> FeatureToggle.isEnabled(Registry.ITEM.getKey(x.getItem())))
                            .collect(Collectors.toList())
            );
        }
    }
}
