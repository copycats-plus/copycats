package com.copycatsplus.copycats;

import com.copycatsplus.copycats.datagen.recipes.FeatureEnabledCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

@SuppressWarnings("unused")
public class CCCraftingConditions {
    public static void register() {
        ResourceConditions.register(FeatureEnabledCondition.NAME, object -> FeatureEnabledCondition.read(object).test());
    }
}
