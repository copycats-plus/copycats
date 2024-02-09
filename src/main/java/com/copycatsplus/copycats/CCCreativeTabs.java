package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CCCreativeTabs {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "copycats" namespace
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Copycats.MODID);

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
            CCBlocks.COPYCAT_LAYER,
            CCBlocks.COPYCAT_SLICE,
            CCBlocks.COPYCAT_VERTICAL_SLICE,
            CCBlocks.COPYCAT_WOOD_BUTTON,
            CCBlocks.COPYCAT_STONE_BUTTON,
            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
            CCBlocks.COPYCAT_IRON_PRESSURE_PLATE,
            CCBlocks.COPYCAT_GOLD_PRESSURE_PLATE
    );

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.copycats.main"))
            .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
            .icon(CCBlocks.COPYCAT_SLAB::asStack)
            .displayItems(new DisplayItemsGenerator(ITEMS))
            .build());

    public static void hideItems(BuildCreativeModeTabContentsEvent event) {
        if (Objects.equals(event.getTabKey(), MAIN.getKey()) || Objects.equals(event.getTabKey(), CreativeModeTabs.SEARCH)) {
            Set<Item> hiddenItems = ITEMS.stream()
                    .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                    .map(ItemProviderEntry::asItem)
                    .collect(Collectors.toSet());
            for (Iterator<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>> iterator = event.getEntries().iterator(); iterator.hasNext(); ) {
                Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry = iterator.next();
                if (hiddenItems.contains(entry.getKey().getItem()))
                    iterator.remove();
            }
        }
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(CCCreativeTabs::hideItems);
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
