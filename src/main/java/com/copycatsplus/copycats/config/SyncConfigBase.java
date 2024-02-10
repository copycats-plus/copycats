package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.Copycats;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

/**
 * Base class for all configs that require custom synchronization from server to clients.
 */
public abstract class SyncConfigBase extends ConfigBase {

    public SimpleChannel syncChannel;
    private Function<CompoundTag, ? extends SyncConfig> messageSupplier;

    public final CompoundTag getSyncConfig() {
        CompoundTag nbt = new CompoundTag();
        writeSyncConfig(nbt);
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    if (nbt.contains(child.getName()))
                        throw new RuntimeException("A sync config key starts with " + child.getName() + " but does not belong to the child");
                    nbt.put(child.getName(), syncChild.getSyncConfig());
                }
            }
        return nbt;
    }

    /**
     * Serialize all configs into the provided `nbt` tag to be synced to clients.
     *
     * @param nbt Accepts any value.
     */
    protected void writeSyncConfig(CompoundTag nbt) {
    }

    public final void setSyncConfig(CompoundTag config) {
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    CompoundTag nbt = config.getCompound(child.getName());
                    syncChild.readSyncConfig(nbt);
                }
            }
        readSyncConfig(config);
    }

    /**
     * Deserialize all configs from the provided `nbt` tag to a custom data storage.
     * The implementing class is responsible for data storage. No storage/config overwrite mechanism is provided here.
     *
     * @param nbt The configs sent from server.
     */
    protected void readSyncConfig(CompoundTag nbt) {
    }

    /**
     * Sets up all aspects of network communication. The implementing class is expected to expose a parameterless
     * register function for consumers, and to call this function inside the register function with all parameters
     * provided by the implementing class.
     * <p>
     * Most parameters should come from an inherited version of {@link SyncConfig}.
     */
    public <T extends SyncConfig> void registerAsSyncRoot(
            String configVersion,
            Class<T> messageType,
            Function<FriendlyByteBuf, T> decoder,
            Function<CompoundTag, T> messageSupplier
    ) {
        syncChannel = new SimpleChannel(Copycats.asResource("config_" + getName()));
        syncChannel.registerS2CPacket(messageType, 0, decoder);
        setMessageSupplier(messageSupplier);
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> syncToPlayer(listener.getPlayer()));
    }

    private <T extends SyncConfig> void setMessageSupplier(Function<CompoundTag, T> messageSupplier) {
        this.messageSupplier = messageSupplier;
    }

    private Function<CompoundTag, ? extends SyncConfig> messageSupplier() {
        return messageSupplier;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        syncToAllPlayers();
    }

    @Override
    public void onReload() {
        super.onReload();
        syncToAllPlayers();
    }

    public void syncToAllPlayers() {
        if (this.syncChannel == null) {
            return; // not sync root
        }
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            Copycats.LOGGER.debug("Sync Config: Config sync skipped due to null server");
            return;
        }
        Copycats.LOGGER.debug("Sync Config: Sending server config to all players on reload");
        syncChannel.sendToClients(messageSupplier().apply(getSyncConfig()), ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers());
    }

    private void syncToPlayer(ServerPlayer player) {
        if (player == null) return;
        Copycats.LOGGER.debug("Sync Config: Sending server config to " + player.getScoreboardName());
        syncChannel.sendToClient(messageSupplier().apply(getSyncConfig()), player);
    }

    /**
     * A helper class to handle network messages. All children of {@link SyncConfigBase} should have a corresponding
     * child of {@link SyncConfig}, with its methods provided to {@link SyncConfigBase#registerAsSyncRoot}.
     */
    public abstract static class SyncConfig extends SimplePacketBase {

        private final CompoundTag nbt;

        protected SyncConfig(CompoundTag nbt) {
            this.nbt = nbt;
        }

        protected SyncConfig(FriendlyByteBuf buf) {
            this(decode(buf));
        }

        protected abstract SyncConfigBase configInstance();

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(nbt);
        }

        static CompoundTag decode(FriendlyByteBuf buf) {
            return buf.readAnySizeNbt();
        }

        @Override
        public boolean handle(SimplePacketBase.Context context) {
            context.enqueueWork(() -> EnvExecutor.runWhenOn(EnvType.CLIENT, () -> this::handleOnClient));
            return true;
        }

        @Environment(EnvType.CLIENT)
        private void handleOnClient() {
            configInstance().setSyncConfig(nbt);
            Copycats.LOGGER.debug("Sync Config: Received and applied server config " + nbt.toString());
        }
    }

}
