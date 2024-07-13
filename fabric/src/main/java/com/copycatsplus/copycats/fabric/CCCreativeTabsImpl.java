package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCCreativeTabs;
import com.copycatsplus.copycats.CopycatRegistrate;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CCCreativeTabsImpl extends CCCreativeTabs {

    public static final AllCreativeModeTabs.TabInfo MAIN_TAB = register("main", () -> FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.copycats.main"))
            .icon(CCBlocks.COPYCAT_SLAB::asStack)
            .displayItems(new DisplayItemsGenerator(DECORATIVE))
            .build());

    public static final AllCreativeModeTabs.TabInfo FUNCTIONAL_TAB = register("functional", () -> FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.copycats.functional"))
            .icon(CCBlocks.COPYCAT_DOOR::asStack)
            .displayItems(new DisplayItemsGenerator(FUNCTIONAL))
            .build());

    public static void setCreativeTab() {
        CopycatRegistrate.setTab(MAIN_TAB.key());
    }

    private static AllCreativeModeTabs.TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = Copycats.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new AllCreativeModeTabs.TabInfo(key, tab);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(CCCreativeTabsImpl.MAIN_TAB.key()).register(CCCreativeTabsImpl::hideDecorItems);
        ItemGroupEvents.modifyEntriesEvent(CCCreativeTabsImpl.FUNCTIONAL_TAB.key()).register(CCCreativeTabsImpl::hideFunctionalItems);
    }

    private static void hideDecorItems(FabricItemGroupEntries event) {
        Set<Item> hiddenItems = DECORATIVE.stream()
                .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                .map(ItemProviderEntry::asItem)
                .collect(Collectors.toSet());
        event.getDisplayStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
        event.getSearchTabStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
    }

    private static void hideFunctionalItems(FabricItemGroupEntries event) {
        Set<Item> hiddenItems = FUNCTIONAL.stream()
                .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                .map(ItemProviderEntry::asItem)
                .collect(Collectors.toSet());
        event.getDisplayStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
        event.getSearchTabStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
    }

    public static CreativeModeTab getBaseTab() {
        return MAIN_TAB.tab();
    }

    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        return MAIN_TAB.key();
    }

    public static CreativeModeTab getFunctionalTab() {
        return FUNCTIONAL_TAB.tab();
    }

    public static ResourceKey<CreativeModeTab> getFunctionalTabKey() {
        return FUNCTIONAL_TAB.key();
    }

}
