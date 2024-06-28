package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.model.functional.FunctionalCopycatRenderHelper;
import com.copycatsplus.copycats.content.copycat.base.model.functional.IFunctionalCopycatBlockRenderer;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.network.CCPackets;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import net.minecraft.client.Minecraft;

public class CopycatsClient {

    public static final SuperByteBufferCache BUFFER_CACHE = new SuperByteBufferCache();

    public static void init() {
        LogicalSidedProvider.setClient(Minecraft::getInstance);
        CCPackets.PACKETS.registerS2CListener();
        BUFFER_CACHE.registerCompartment(FunctionalCopycatRenderHelper.KINETIC_COPYCAT, 60);
    }
}
