package com.copycatsplus.copycats;

import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.network.CCPackets;
import net.minecraft.client.Minecraft;

public class CopycatsClient {

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        CCPackets.PACKETS.registerS2CListener();
    }
}
