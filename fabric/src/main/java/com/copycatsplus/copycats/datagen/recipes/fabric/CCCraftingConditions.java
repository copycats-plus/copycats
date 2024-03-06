package com.copycatsplus.copycats.datagen.recipes.fabric;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

public class CCCraftingConditions {

    public static void register() {
        ResourceConditions.register(FeatureEnabledCondition.NAME, object -> FeatureEnabledCondition.read(object).test());
    }
}
