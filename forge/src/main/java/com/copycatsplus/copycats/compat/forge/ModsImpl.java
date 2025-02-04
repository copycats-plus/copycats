package com.copycatsplus.copycats.compat.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ModsImpl {


    public static boolean getLoaded(String id) {
        // If ModList is null, we are in the loading stage, so we need to check the loading mod list
        if (ModList.get() == null)
            return FMLLoader.getLoadingModList().getMods().stream().anyMatch(mod -> mod.getModId().equals(id));
        return ModList.get().isLoaded(id);
    }
}
