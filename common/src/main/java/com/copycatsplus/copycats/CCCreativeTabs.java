package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.mixin_interfaces.CreativeTabExpander;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CCCreativeTabs {

    public static final CreativeModeTab BASE = new BaseCreativeModeTab();
    public static final CreativeModeTab FUNCTION = new FunctionalCreativeModeTab();

    public static final List<ItemProviderEntry<?>> DECORATIVE = List.of(
            /* Vanilla blocks */
            CCBlocks.COPYCAT_BLOCK,
            CCBlocks.COPYCAT_SLAB,
            CCBlocks.COPYCAT_STAIRS,
            CCBlocks.COPYCAT_VERTICAL_STAIRS,
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
            CCBlocks.COPYCAT_FENCE,
            CCBlocks.COPYCAT_FENCE_GATE,
            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_IRON_DOOR,
            CCBlocks.COPYCAT_IRON_TRAPDOOR,
            CCBlocks.COPYCAT_LADDER,
            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_STONE_BUTTON,
            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
            CCBlocks.COPYCAT_TRAPDOOR,
            CCBlocks.COPYCAT_WOODEN_BUTTON,
            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,

            /* Create */
            CCBlocks.COPYCAT_FLUID_PIPE,
            CCBlocks.COPYCAT_SHAFT,
            CCBlocks.COPYCAT_COGWHEEL,
            CCBlocks.COPYCAT_LARGE_COGWHEEL
    );

    public static class BaseCreativeModeTab extends CreativeModeTab {

        public BaseCreativeModeTab() {
            super(((CreativeTabExpander) CreativeModeTab.TAB_BUILDING_BLOCKS).copycats$expandTabCount(), Copycats.MODID + ".main");
        }

        @Override
        public ItemStack makeIcon() {
            return CCBlocks.COPYCAT_SLAB.asStack();
        }

        @Override
        public void fillItemList(@NotNull NonNullList<ItemStack> pItems) {
            for (ItemProviderEntry<?> item : DECORATIVE) {
                if (FeatureToggle.isEnabled(item.getId()))
                    item.get().asItem().fillItemCategory(this, pItems);
            }
        }
    }

    public static class FunctionalCreativeModeTab extends CreativeModeTab {

        public FunctionalCreativeModeTab() {
            super(((CreativeTabExpander) CreativeModeTab.TAB_BUILDING_BLOCKS).copycats$expandTabCount(), Copycats.MODID + ".functional");
        }

        @Override
        public ItemStack makeIcon() {
            return CCBlocks.COPYCAT_DOOR.asStack();
        }

        @Override
        public void fillItemList(@NotNull NonNullList<ItemStack> pItems) {
            for (ItemProviderEntry<?> item : FUNCTIONAL) {
                if (FeatureToggle.isEnabled(item.getId()))
                    item.get().asItem().fillItemCategory(this, pItems);
            }
        }
    }
}
