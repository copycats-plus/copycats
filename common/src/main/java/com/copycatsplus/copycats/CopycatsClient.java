package com.copycatsplus.copycats;

import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import com.copycatsplus.copycats.network.CCPackets;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import net.minecraft.client.Minecraft;

public class CopycatsClient {

    public static final SuperByteBufferCache BUFFER_CACHE = new SuperByteBufferCache();

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        CCPackets.PACKETS.registerS2CListener();
        BUFFER_CACHE.registerCompartment(KineticCopycatRenderer.KINETIC_COPYCAT, 60);
    }

    public static void invalidateCaches() {
        BUFFER_CACHE.invalidate();
    }
}
