package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    @ExpectPlatform
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        throw new AssertionError("This should never appear!");
    }
}
