package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.Copycats;

public class CCPackets {

    //TODO: Increase version on updates
    public static final PacketSystem PACKETS = PacketSystem.builder(Copycats.MODID, 1)
            .s2c(ConfigSyncPacket.class, ConfigSyncPacket::new)
            .build();
}
