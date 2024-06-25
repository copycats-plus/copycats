package com.copycatsplus.copycats.utility.fabric;

import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtilsImpl {

    public static CompoundTag serializeStack(ItemStack stack) {
        return (CompoundTag) NBTSerializer.serializeNBT(stack);
    }
}
