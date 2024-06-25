package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {

    @ExpectPlatform
    public static CompoundTag serializeStack(ItemStack stack) {
        throw new AssertionError("This shouldn't appear");
    }
}
