package com.copycatsplus.copycats.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CClient extends ConfigBase {

    @Override
    public String getName() {
        return "client";
    }

    public final ConfigBool useEnhancedModels = b(true, "useEnhancedModels", Comments.useEnhancedModels);

    private static class Comments {
        static String useEnhancedModels = "Use more complex copycat models to improve appearance with certain materials.";
    }
}
