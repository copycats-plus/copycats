package com.copycatsplus.copycats.compat.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModsImpl {


    public static boolean getLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}
