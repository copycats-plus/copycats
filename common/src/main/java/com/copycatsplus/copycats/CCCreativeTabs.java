package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CCCreativeTabs {

    public static final List<ItemProviderEntry<?>> ITEMS = List.of(
            CCBlocks.COPYCAT_BEAM,
            CCBlocks.COPYCAT_BLOCK,
            CCBlocks.COPYCAT_BOARD,
            CCItems.COPYCAT_BOX,
            CCBlocks.COPYCAT_BYTE,
            CCItems.COPYCAT_CATWALK,
            CCBlocks.COPYCAT_FENCE,
            CCBlocks.COPYCAT_FENCE_GATE,
            CCBlocks.COPYCAT_HALF_LAYER,
            CCBlocks.COPYCAT_HALF_PANEL,
            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_LAYER,
            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_SLAB,
            CCBlocks.COPYCAT_SLICE,
            CCBlocks.COPYCAT_STAIRS,
            CCBlocks.COPYCAT_STONE_BUTTON,
            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
            CCBlocks.COPYCAT_TRAPDOOR,
            CCBlocks.COPYCAT_VERTICAL_SLICE,
            CCBlocks.COPYCAT_VERTICAL_STEP,
            CCBlocks.COPYCAT_WALL,
            CCBlocks.COPYCAT_WOODEN_BUTTON,
            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
            CCBlocks.COPYCAT_CONFIGURABLE_BLOCK
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

    public record DisplayItemsGenerator(List<ItemProviderEntry<?>> items) implements CreativeModeTab.DisplayItemsGenerator {
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
