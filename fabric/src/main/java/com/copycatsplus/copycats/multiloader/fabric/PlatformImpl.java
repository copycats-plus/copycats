package com.copycatsplus.copycats.multiloader.fabric;

import com.copycatsplus.copycats.multiloader.Platform;
import com.copycatsplus.copycats.multiloader.Platform.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformImpl {

    public static Platform getCurrent() {
        return Platform.FABRIC;
    }
}
