package com.copycatsplus.copycats.config.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.SyncConfigBase;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.network.ConfigSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public abstract class SyncConfigBaseImpl extends SyncConfigBase {

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        syncToPlayer((ServerPlayer) event.getEntity());
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
    }
}
