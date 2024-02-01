package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.SyncConfigBase;
import net.fabricmc.api.ClientModInitializer;

public class CopycatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SyncConfigBase.syncChannel.initClientListener();
    }
}
