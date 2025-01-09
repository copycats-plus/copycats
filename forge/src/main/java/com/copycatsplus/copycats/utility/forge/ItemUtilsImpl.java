package com.copycatsplus.copycats.utility.forge;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemUtilsImpl {

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        return ItemHandlerHelper.copyStackWithSize(itemStack, size);
    }

    public static Tag serializeNBT(ItemStack stack) {
        return stack.serializeNBT();
    }
}
