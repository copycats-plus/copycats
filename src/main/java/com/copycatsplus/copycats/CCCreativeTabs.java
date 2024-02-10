package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CCCreativeTabs {
    public static final CreativeModeTab MAIN = new MainCreativeModeTab();

    public static final List<ItemProviderEntry<?>> ITEMS = List.of(
            CCBlocks.COPYCAT_BLOCK,
            CCBlocks.COPYCAT_SLAB,
            CCBlocks.COPYCAT_BEAM,
            CCBlocks.COPYCAT_VERTICAL_STEP,
            CCBlocks.COPYCAT_HALF_PANEL,
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
            CCBlocks.COPYCAT_HALF_LAYER,
            CCBlocks.COPYCAT_SLICE,
            CCBlocks.COPYCAT_VERTICAL_SLICE,
            CCBlocks.COPYCAT_WOODEN_BUTTON,
            CCBlocks.COPYCAT_STONE_BUTTON,
            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE
    );

    public static void register() {
    }

    public static class MainCreativeModeTab extends CreativeModeTab {

        public MainCreativeModeTab() {
            super(ItemGroupUtil.expandArrayAndGetId(), Copycats.MODID + ".main");
        }

        @Override
        public ItemStack makeIcon() {
            return CCBlocks.COPYCAT_SLAB.asStack();
        }

        @Override
        public void fillItemList(@NotNull NonNullList<ItemStack> pItems) {
            for (ItemProviderEntry<?> item : ITEMS) {
                if (FeatureToggle.isEnabled(item.getId()))
                    item.get().asItem().fillItemCategory(this, pItems);
            }
        }
    }
}
