package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CCCreativeTabs {

    public static final List<@NotNull ItemProviderEntry<? extends FeatureElement, ? extends FeatureElement>> DECORATIVE = List.of(
            /* Vanilla blocks */
            CCBlocks.COPYCAT_BLOCK,
            CCBlocks.COPYCAT_SLAB,
            CCBlocks.COPYCAT_STAIRS,
            CCBlocks.COPYCAT_VERTICAL_STAIRS,
            CCBlocks.COPYCAT_FENCE,
            CCBlocks.COPYCAT_WALL,

            /* Simple copycats */
            CCBlocks.COPYCAT_VERTICAL_STEP,
            CCBlocks.COPYCAT_BEAM,
            CCBlocks.COPYCAT_SLICE,
            CCBlocks.COPYCAT_VERTICAL_SLICE,
            CCBlocks.COPYCAT_CORNER_SLICE,
            CCBlocks.COPYCAT_GHOST_BLOCK,
            CCBlocks.COPYCAT_LAYER,
            CCBlocks.COPYCAT_HALF_PANEL,
            CCBlocks.COPYCAT_PANE,
            CCBlocks.COPYCAT_FLAT_PANE,
            /* Multi-states */
            CCBlocks.COPYCAT_BYTE,
            CCBlocks.COPYCAT_BYTE_PANEL,
            CCBlocks.COPYCAT_BOARD,
            CCItems.COPYCAT_CATWALK,
            CCItems.COPYCAT_BOX,
            CCBlocks.COPYCAT_HALF_LAYER,
            CCBlocks.COPYCAT_VERTICAL_HALF_LAYER,
            CCBlocks.COPYCAT_STACKED_HALF_LAYER,
            /* Slopes */
            CCBlocks.COPYCAT_SLOPE,
            CCBlocks.COPYCAT_VERTICAL_SLOPE,
            CCBlocks.COPYCAT_SLOPE_LAYER
    );

    public static final List<ItemProviderEntry<? extends FeatureElement, ? extends FeatureElement>> FUNCTIONAL = List.of(
            /* Vanilla */
            CCBlocks.COPYCAT_DOOR,
            CCBlocks.COPYCAT_IRON_DOOR,
            CCBlocks.COPYCAT_SLIDING_DOOR,
            CCBlocks.COPYCAT_FOLDING_DOOR,
            CCBlocks.COPYCAT_TRAPDOOR,
            CCBlocks.COPYCAT_IRON_TRAPDOOR,
            CCBlocks.COPYCAT_REDSTONE_LAMP,
            CCBlocks.COPYCAT_FENCE_GATE,
            CCBlocks.COPYCAT_WOODEN_BUTTON,
            CCBlocks.COPYCAT_STONE_BUTTON,
            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_LADDER,

            /* Create */
            CCBlocks.COPYCAT_FLUID_PIPE,
            CCBlocks.COPYCAT_SHAFT,
            CCBlocks.COPYCAT_COGWHEEL,
            CCBlocks.COPYCAT_LARGE_COGWHEEL
    );

    @ExpectPlatform
    public static void setCreativeTab() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static CreativeModeTab getBaseTab() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> getBaseTabKey() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static CreativeModeTab getFunctionalTab() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ResourceKey<CreativeModeTab> getFunctionalTabKey() {
        throw new AssertionError();
    }

    public record AdvancedDisplayGenerator(
            List<ItemProviderEntry<? extends FeatureElement, ? extends FeatureElement>> items,
            Supplier<CreativeModeTab> currentTab) implements CreativeModeTab.DisplayItemsGenerator {

        private List<ItemOrdering> makeOrderings() {
            List<ItemOrdering> orderings = new ReferenceArrayList<>();

            Map<ItemProviderEntry<?, ?>, ItemProviderEntry<?, ?>> simpleBeforeOrderings = Map.of(
                    CCBlocks.COPYCAT_WOODEN_BUTTON, CCBlocks.COPYCAT_STONE_BUTTON,
                    CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE, CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
                    CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE, CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE
            );

            Map<ItemProviderEntry<?, ?>, ItemProviderEntry<?, ?>> simpleAfterOrderings = Map.of(
                    CCBlocks.COPYCAT_IRON_TRAPDOOR, CCBlocks.COPYCAT_TRAPDOOR,
                    CCBlocks.COPYCAT_IRON_DOOR, CCBlocks.COPYCAT_DOOR,
                    CCBlocks.COPYCAT_SLIDING_DOOR, CCBlocks.COPYCAT_FOLDING_DOOR
            );

            simpleBeforeOrderings.forEach((entry, otherEntry) -> {
                if (items.contains(entry) && items.contains(otherEntry))
                    orderings.add(ItemOrdering.before(entry.asItem(), otherEntry.asItem()));
            });

            simpleAfterOrderings.forEach((entry, otherEntry) -> {
                if (items.contains(entry) && items.contains(otherEntry))
                    orderings.add(ItemOrdering.after(entry.asItem(), otherEntry.asItem()));
            });

            return orderings;
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            List<ItemOrdering> orderings = makeOrderings();

            List<Item> itemList = new LinkedList<>(items.stream().map(ItemProviderEntry::asItem).toList());
            applyOrderings(itemList, orderings);
            for (Item item : itemList) {
                if (FeatureToggle.isEnabled(BuiltInRegistries.ITEM.getKey(item)))
                    output.accept(item);
            }
        }

        private static void applyOrderings(List<Item> items, List<ItemOrdering> orderings) {
            for (ItemOrdering ordering : orderings) {
                int anchorIndex = items.indexOf(ordering.anchor());
                if (anchorIndex != -1) {
                    Item item = ordering.item();
                    int itemIndex = items.indexOf(item);
                    if (itemIndex != -1) {
                        items.remove(itemIndex);
                        if (itemIndex < anchorIndex) {
                            anchorIndex--;
                        }
                    }
                    if (ordering.type() == ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    } else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        private record ItemOrdering(Item item, Item anchor, Type type) {
            public static ItemOrdering before(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, Type.BEFORE);
            }

            public static ItemOrdering after(Item item, Item anchor) {
                return new ItemOrdering(item, anchor, Type.AFTER);
            }

            public enum Type {
                BEFORE,
                AFTER;
            }
        }
    }
}
