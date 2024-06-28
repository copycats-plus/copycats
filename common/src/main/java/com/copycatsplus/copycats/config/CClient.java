package com.copycatsplus.copycats.config;

import net.minecraftforge.fml.config.ModConfig;

//Should never be synced just did it to allow registration
public class CClient extends SyncConfigBase {

    @Override
    protected ModConfig.Type type() {
        return ModConfig.Type.CLIENT;
    }

    @Override
    public String getName() {
        return "client";
    }

    public final ConfigBool useEnhancedModels = b(true, "useEnhancedModels", Comments.useEnhancedModels);
    public final ConfigBool disableGraphicsWarnings = b(false, "disableGraphicsWarnings", Comments.disableGraphicsWarnings);

    private static class Comments {
        static String useEnhancedModels = "Use more complex copycat models to improve appearance with certain materials.";
        static String disableGraphicsWarnings = "Disable warnings about graphics settings that may cause issues with the mod.";
    }
}
