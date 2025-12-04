package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.RendererReloadCache;
import dev.engine_room.flywheel.api.event.ReloadLevelRendererEvent;
import net.minecraftforge.common.MinecraftForge;

public class CopycatsClientImpl {

    public static void init() {
        CopycatsClient.init();
        MinecraftForge.EVENT_BUS.<ReloadLevelRendererEvent>addListener(reloadLevelRendererEvent -> {
            for (RendererReloadCache<?, ?> cache : RendererReloadCache.getALL()) {
                cache.clear();
            }
        });
    }
}
