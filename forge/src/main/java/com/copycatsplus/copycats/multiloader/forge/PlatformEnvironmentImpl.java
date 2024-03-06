package com.copycatsplus.copycats.multiloader.forge;

import com.copycatsplus.copycats.multiloader.Platform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class PlatformEnvironmentImpl {

    public static Platform.Environment getCurrent() {
        return FMLEnvironment.dist == Dist.CLIENT ? Platform.Environment.CLIENT : Platform.Environment.SERVER;
    }
}
