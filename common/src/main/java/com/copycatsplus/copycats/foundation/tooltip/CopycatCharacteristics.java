package com.copycatsplus.copycats.foundation.tooltip;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum CopycatCharacteristics implements StringRepresentable {
    COPYCAT("Copycat", "R-click with material to _assign_ and _rotate_. Wrench to _remove material_."),
    CT_TOGGLE("CT Toggle", "Shift-R-click with empty hand to _toggle connected textures_."),
    MULTI_STATE("Multi-state", "Put _multiple copies_ with different materials in the same block space."),
    STACKABLE("Stackable", "R-click with the same copycat to _enlarge_."),
    FUNCTIONAL("Functional", "_Same usage_ as non-copycat counterparts."),
    GHOST("Ghost", "_No collision_ with entities."),
    PRE_ASSEMBLED("Pre-assembled", "Disassemble _individual parts_ after placement."),
    COPY_CAT("???", "R-click on a _Cat_.");

    private final String title;
    private final String description;

    CopycatCharacteristics(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public String getTitleKey() {
        return "tooltip.copycats.characteristics." + getSerializedName() + ".title";
    }

    public String getTitle() {
        return title;
    }

    public String getDescriptionKey() {
        return "tooltip.copycats.characteristics." + getSerializedName() + ".description";
    }

    public String getDescription() {
        return description;
    }

    public static List<CopycatCharacteristics> all() {
        return List.of(values());
    }
}
