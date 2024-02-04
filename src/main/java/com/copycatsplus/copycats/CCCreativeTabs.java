package com.copycatsplus.copycats;

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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CCCreativeTabs {

    public static final List<ItemProviderEntry<?>> ITEMS = List.of(
            CCBlocks.COPYCAT_BLOCK,
            CCBlocks.COPYCAT_SLAB,
            CCBlocks.COPYCAT_BEAM,
            CCBlocks.COPYCAT_VERTICAL_STEP,
            CCBlocks.COPYCAT_STAIRS,
            CCBlocks.COPYCAT_FENCE,
            CCBlocks.COPYCAT_FENCE_GATE,
            CCBlocks.COPYCAT_TRAPDOOR,
            CCBlocks.COPYCAT_WALL,
            CCBlocks.COPYCAT_BOARD,
            CCItems.COPYCAT_BOX,
            CCItems.COPYCAT_CATWALK,
            CCBlocks.COPYCAT_BYTE,
            CCBlocks.COPYCAT_LAYER
    );

    public static final AllCreativeModeTabs.TabInfo MAIN = register("main", () -> FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.copycats.main"))
            .icon(CCBlocks.COPYCAT_SLAB::asStack)
            .displayItems(new DisplayItemsGenerator(ITEMS))
            .build());

    public static void hideItems(FabricItemGroupEntries event) {
        Set<Item> hiddenItems = ITEMS.stream()
                .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                .map(ItemProviderEntry::asItem)
                .collect(Collectors.toSet());
        event.getDisplayStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
        event.getSearchTabStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
    }

    private static AllCreativeModeTabs.TabInfo register(String name, Supplier<CreativeModeTab> supplier) {
        ResourceLocation id = Copycats.asResource(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
        CreativeModeTab tab = supplier.get();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
        return new AllCreativeModeTabs.TabInfo(key, tab);
    }

    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(CCCreativeTabs.MAIN.key()).register(CCCreativeTabs::hideItems);
    }

    private record DisplayItemsGenerator(
            List<ItemProviderEntry<?>> items) implements CreativeModeTab.DisplayItemsGenerator {
        @Override
        public void accept(@NotNull CreativeModeTab.ItemDisplayParameters params, @NotNull CreativeModeTab.Output output) {
            for (ItemProviderEntry<?> item : items) {
                if (FeatureToggle.isEnabled(item.getId())) {
                    output.accept(item);
                }
            }
        }
    }
}
