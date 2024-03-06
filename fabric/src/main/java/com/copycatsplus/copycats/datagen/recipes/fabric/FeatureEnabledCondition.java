package com.copycatsplus.copycats.datagen.recipes.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class FeatureEnabledCondition implements ConditionJsonProvider {
    public static final ResourceLocation NAME = Copycats.asResource("feature_enabled");
    private final ResourceLocation feature;
    private final boolean invert;

    public FeatureEnabledCondition(ResourceLocation feature, boolean invert) {
        this.feature = feature;
        this.invert = invert;
    }

    public FeatureEnabledCondition(ResourceLocation feature) {
        this(feature, false);
    }

    @Override
    public ResourceLocation getConditionId() {
        return NAME;
    }

    public boolean test() {
        return FeatureToggle.isEnabled(feature) != invert;
    }

    @Override
    public void writeParameters(JsonObject object) {
        object.addProperty("feature", feature.toString());
        object.addProperty("invert", invert);
    }

    public static FeatureEnabledCondition read(JsonObject object) {
        return new FeatureEnabledCondition(
                new ResourceLocation(GsonHelper.getAsString(object, "feature")),
                GsonHelper.getAsBoolean(object, "invert")
        );
    }
}
