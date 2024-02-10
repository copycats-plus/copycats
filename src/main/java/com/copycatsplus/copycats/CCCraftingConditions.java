package com.copycatsplus.copycats;

import com.copycatsplus.copycats.datagen.recipes.FeatureEnabledCondition;
import net.minecraftforge.common.crafting.CraftingHelper;

@SuppressWarnings("unused")
public class CCCraftingConditions {
    public static void register() {
        CraftingHelper.register(FeatureEnabledCondition.Serializer.INSTANCE);
    }
}
