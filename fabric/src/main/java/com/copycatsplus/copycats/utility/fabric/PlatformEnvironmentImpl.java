package com.copycatsplus.copycats.utility.fabric;

import com.copycatsplus.copycats.utility.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformEnvironmentImpl {

    public static Platform.Environment getCurrent() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Platform.Environment.CLIENT : Platform.Environment.SERVER;
    }
}
