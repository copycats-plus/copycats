package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import net.fabricmc.api.ClientModInitializer;

public class CopycatsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CCConfigs.common().syncChannel.initClientListener();
    }
}
