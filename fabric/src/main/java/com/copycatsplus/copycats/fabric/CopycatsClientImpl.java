package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CopycatsClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class CopycatsClientImpl implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CopycatsClient.init();

        ServerChunkEvents.CHUNK_UNLOAD.register(CopycatsImpl::onChunkUnload);
        ServerWorldEvents.UNLOAD.register(CopycatsImpl::onLevelUnload);
    }
}
