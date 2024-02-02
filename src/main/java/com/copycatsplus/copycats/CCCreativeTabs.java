package com.copycatsplus.copycats;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class CCCreativeTabs {

    public static final NonNullList<ItemStack> ITEMS = NonNullList.of(
            ItemStack.EMPTY,
            CCBlocks.COPYCAT_BLOCK.asStack(),
            CCBlocks.COPYCAT_SLAB.asStack(),
            CCBlocks.COPYCAT_BEAM.asStack(),
            CCBlocks.COPYCAT_VERTICAL_STEP.asStack(),
            CCBlocks.COPYCAT_STAIRS.asStack(),
            CCBlocks.COPYCAT_FENCE.asStack(),
            CCBlocks.COPYCAT_FENCE_GATE.asStack(),
            CCBlocks.COPYCAT_TRAPDOOR.asStack(),
            CCBlocks.COPYCAT_WALL.asStack(),
            CCBlocks.COPYCAT_BOARD.asStack(),
            CCItems.COPYCAT_BOX.asStack(),
            CCItems.COPYCAT_CATWALK.asStack(),
            CCBlocks.COPYCAT_BYTE.asStack(),
            CCBlocks.COPYCAT_LAYER.asStack()
    );

    public static final CCCreativeModeTab MAIN = new CCCreativeModeTab("main") {
        @Override
        public ItemStack makeIcon() {
            return CCBlocks.COPYCAT_SLAB.asStack();
        }
    };

    public static void register() {
        //Doesnt exist in 1.19.2
        /*ItemGroupEvents.modifyEntriesEvent(CCCreativeTabs.MAIN).register(CCCreativeTabs::hideItems);*/
    }

/*    public static void hideItems(FabricItemGroupEntries event) {
        Set<Item> hiddenItems = ITEMS.stream()
                .filter(x -> !FeatureToggle.isEnabled(x.getId()))
                .map(ItemProviderEntry::asItem)
                .collect(Collectors.toSet());
        event.getDisplayStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
        event.getSearchTabStacks().removeIf(entry -> hiddenItems.contains(entry.getItem()));
    }*/

/*    private record DisplayItemsGenerator(List<ItemProviderEntry<?>> items) implements CreativeModeTab.DisplayItemsGenerator {
        @Override
        public void accept(@NotNull CreativeModeTab.ItemDisplayParameters params, @NotNull CreativeModeTab.Output output) {
            for (ItemProviderEntry<?> item : items) {
                if (FeatureToggle.isEnabled(item.getId())) {
                    output.accept(item);
                }
            }
        }
    }*/


    //Copied from create
    public abstract static class CCCreativeModeTab extends CreativeModeTab {

        public CCCreativeModeTab(String id) {
            super(ItemGroupUtil.expandArrayAndGetId(), Copycats.MODID + "." + id);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            addItems(items, true);
            addBlocks(items);
            addItems(items, false);
        }

        protected Collection<RegistryEntry<Item>> registeredItems() {
            return Copycats.getRegistrate().getAll(Registry.ITEM_REGISTRY);
        }

        public void addBlocks(NonNullList<ItemStack> items) {
            for (RegistryEntry<Item> entry : registeredItems())
                if (entry.get() instanceof BlockItem blockItem)
                    blockItem.fillItemCategory(this, items);
        }

        public void addItems(NonNullList<ItemStack> items, boolean specialItems) {
            for (RegistryEntry<Item> entry : registeredItems()) {
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                ItemStack stack = new ItemStack(item);
                if (isGui3d(stack) == specialItems)
                    item.fillItemCategory(this, items);
            }
        }

        // fabric: some mods (polymer) may load item groups on servers
        private static boolean isGui3d(ItemStack stack) {
            return EnvExecutor.unsafeRunForDist(
                    () -> () -> Minecraft.getInstance().getItemRenderer()
                            .getModel(stack, null, null, 0).isGui3d(),
                    () -> () -> stack.getItem() instanceof BlockItem // best guess
            );
        }
    }
}
