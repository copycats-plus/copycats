package com.copycatsplus.copycats.utility.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemUtilsImpl {

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        return ItemHandlerHelper.copyStackWithSize(itemStack, size);
    }
}
