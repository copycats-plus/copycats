package com.copycatsplus.copycats.utility.forge;

import com.simibubi.create.foundation.item.TooltipModifier;
import org.jetbrains.annotations.NotNull;

public class TooltipUtilsImpl {

    @NotNull
    public static TooltipModifier sequential(TooltipModifier... modifiers) {
        return event -> {
            for (TooltipModifier modifier : modifiers) {
                modifier.modify(event);
            }
        };
    }
}
