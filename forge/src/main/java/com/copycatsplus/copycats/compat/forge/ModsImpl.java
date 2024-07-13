package com.copycatsplus.copycats.compat.forge;

import net.minecraftforge.fml.ModList;

public class ModsImpl {


    public static boolean getLoaded(String id) {
        return ModList.get().isLoaded(id);
    }
}
