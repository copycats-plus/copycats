package com.copycatsplus.copycats.config;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum FeatureCategory implements StringRepresentable {
    SLOPES("All copycats with a sloped surface"),
    MULTISTATES("All copycats that support multiple materials in a single block"),
    STACKABLES("All copycats that can be resized by putting in more of the same copycat"),
    REDSTONE("All copycats that can emit a redstone signal"),
    FUNCTIONAL("All copycats with extra functions/interactions"),
    CREATE("All copycats mimicking blocks from Create"),
    ;

    private final String description;

    FeatureCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static FeatureCategory byName(String name) {
        for (FeatureCategory category : values()) {
            if (category.getSerializedName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
