package com.copycatsplus.copycats.datagen.recipes;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.google.gson.JsonObject;
import com.copycatsplus.copycats.Copycats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class FeatureEnabledCondition implements ICondition {
    private static final ResourceLocation NAME = Copycats.asResource("feature_enabled");
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
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(IContext context) {
        return FeatureToggle.isEnabled(feature) != invert;
    }

    public static class Serializer implements IConditionSerializer<FeatureEnabledCondition> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, FeatureEnabledCondition value) {
            json.addProperty("feature", value.feature.toString());
            json.addProperty("invert", value.invert);
        }

        @Override
        public FeatureEnabledCondition read(JsonObject json) {
            return new FeatureEnabledCondition(
                    new ResourceLocation(GsonHelper.getAsString(json, "feature")),
                    GsonHelper.getAsBoolean(json, "invert")
            );
        }

        @Override
        public ResourceLocation getID() {
            return NAME;
        }
    }
}
