package com.copycatsplus.copycats.datagen.recipes.forge;

import net.minecraftforge.common.crafting.CraftingHelper;

public class CCCraftingConditions {

    public static void register() {
        CraftingHelper.register(FeatureEnabledCondition.Serializer.INSTANCE);
    }
}
