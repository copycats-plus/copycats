package com.copycatsplus.copycats.compat.recipe_viewers;

import com.copycatsplus.copycats.CCCreativeTabs;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class CopycatsEMI implements EmiPlugin {

    public static EmiRegistry REGISTRY;

    @Override
    public void register(EmiRegistry emiRegistry) {
        REGISTRY = emiRegistry;
    }

    //Currently doesn't refresh. But does update on world join but now at least the stacks vanish whereas before this plugin they didnt
    public static void refreshItemList() {
        if (REGISTRY != null) {
            CCCreativeTabs.DECORATIVE.stream()
                    .map(ItemProviderEntry::asStack)
                    .map(EmiStack::of)
                    .forEach(REGISTRY::removeEmiStacks);
            CCCreativeTabs.FUNCTIONAL.stream()
                    .map(ItemProviderEntry::asStack)
                    .map(EmiStack::of)
                    .forEach(REGISTRY::removeEmiStacks);

            CCCreativeTabs.DECORATIVE.stream()
                    .filter(x -> FeatureToggle.isEnabled(x.getId()))
                    .map(ItemProviderEntry::asStack)
                    .map(EmiStack::of)
                    .forEach(REGISTRY::addEmiStack);
            CCCreativeTabs.FUNCTIONAL.stream()
                    .filter(x -> FeatureToggle.isEnabled(x.getId()))
                    .map(ItemProviderEntry::asStack)
                    .map(EmiStack::of)
                    .forEach(REGISTRY::addEmiStack);
        }
    }
}
