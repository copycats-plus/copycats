package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CCCreativeTabs {

    public static final List<ItemProviderEntry<?>> DECORATIVE = List.of(
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
            CCBlocks.COPYCAT_GHOST_BLOCK,
            CCBlocks.COPYCAT_LAYER,
            CCBlocks.COPYCAT_HALF_PANEL,
            /* Multi-states */
            CCBlocks.COPYCAT_BYTE,
            CCBlocks.COPYCAT_BOARD,
            CCItems.COPYCAT_CATWALK,
            CCItems.COPYCAT_BOX,
            CCBlocks.COPYCAT_HALF_LAYER,
            /* Slopes */
            CCBlocks.COPYCAT_SLOPE,
            CCBlocks.COPYCAT_VERTICAL_SLOPE,
            CCBlocks.COPYCAT_SLOPE_LAYER
    );

    public static final List<ItemProviderEntry<?>> FUNCTIONAL = List.of(
            /* Vanilla */
            CCBlocks.COPYCAT_DOOR,
            CCBlocks.COPYCAT_IRON_DOOR,
            CCBlocks.COPYCAT_SLIDING_DOOR,
            CCBlocks.COPYCAT_FOLDING_DOOR,
            CCBlocks.COPYCAT_TRAPDOOR,
            CCBlocks.COPYCAT_IRON_TRAPDOOR,
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


    public record DisplayItemsGenerator(
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
