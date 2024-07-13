package com.copycatsplus.copycats.utility.fabric;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemUtilsImpl {

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        return ItemHandlerHelper.copyStackWithSize(itemStack, size);
    }

    public static Tag serializeNBT(ItemStack stack) {
        return NBTSerializer.serializeNBT(stack);
    }
}
