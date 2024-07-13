package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemUtils {

    /**
     * Platform-independent implementation of copying an ItemStack with a different size.
     */
    @ExpectPlatform
    @NotNull
    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        //noinspection DataFlowIssue
        return null;
    }

    /**
     * Platform-independent implementation of serializing an ItemStack to NBT.
     */
    @ExpectPlatform
    @NotNull
    public static Tag serializeNBT(ItemStack stack) {
        //noinspection DataFlowIssue
        return null;
    }
}
