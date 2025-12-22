package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.RendererReloadCache;
import dev.engine_room.flywheel.api.event.ReloadLevelRendererEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.ApiStatus;

public class CopycatsClientImpl {

    public static void init() {
        CopycatsClient.init();
        MinecraftForge.EVENT_BUS.addListener(CopycatsClientImpl::onReloadLevelRenderer);
    }

    @ApiStatus.Internal
    public static void onReloadLevelRenderer(ReloadLevelRendererEvent e) {
        for (RendererReloadCache<?, ?> cache : RendererReloadCache.ALL) {
            cache.clear();
        }
    }
}
