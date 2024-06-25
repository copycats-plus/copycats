package com.copycatsplus.copycats.utility.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtilsImpl {

    public static CompoundTag serializeStack(ItemStack stack) {
        return stack.serializeNBT();
    }
}
