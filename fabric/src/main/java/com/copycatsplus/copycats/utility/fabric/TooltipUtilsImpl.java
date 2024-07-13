package com.copycatsplus.copycats.utility.fabric;

import com.simibubi.create.foundation.item.TooltipModifier;
import org.jetbrains.annotations.NotNull;

public class TooltipUtilsImpl {

    @NotNull
    public static TooltipModifier sequential(TooltipModifier... modifiers) {
        return (stack, player, flags, tooltips) -> {
            for (TooltipModifier modifier : modifiers) {
                modifier.modify(stack, player, flags, tooltips);
            }
        };
    }
}
