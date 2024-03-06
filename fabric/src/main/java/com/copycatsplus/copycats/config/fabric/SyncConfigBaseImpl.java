package com.copycatsplus.copycats.config.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.SyncConfigBase;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.network.ConfigSyncPacket;
import com.copycatsplus.copycats.network.PlayerSelection;
import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public abstract class SyncConfigBaseImpl extends SyncConfigBase {

    private static SyncConfigBaseImpl instance;


    public void register() {
        instance = this;
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> syncToPlayer(listener.getPlayer()));
    }

    public void syncToPlayer(ServerPlayer player) {
        if (player == null) return;
        CCPackets.PACKETS.sendTo(player, new ConfigSyncPacket(getSyncConfig(), type()));
        Copycats.LOGGER.debug("Sync Config: Sending server config to " + player.getName().getString());
    }

    public static void syncToAllPlayers() {
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            Copycats.LOGGER.debug("Sync Config: Config sync skipped due to null server");
            return;
        }
        Copycats.LOGGER.debug("Sync Config: Sending server config to all players on reload");
        CCPackets.PACKETS.sendTo(PlayerSelection.all(), new ConfigSyncPacket(instance.getSyncConfig(), instance.type()));
    }
}
