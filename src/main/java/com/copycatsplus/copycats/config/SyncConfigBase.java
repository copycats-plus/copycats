package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.Copycats;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SyncConfigBase extends ConfigBase {

    private SimpleChannel syncChannel;
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

    protected void readSyncConfig(CompoundTag nbt) {
    }

    public <T extends SyncConfig> void registerAsSyncRoot(
            String configVersion,
            Class<T> messageType,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<Context>> messageConsumer,
            Function<CompoundTag, T> messageSupplier
    ) {
        syncChannel = NetworkRegistry.newSimpleChannel(
                Copycats.asResource("config_" + getName()),
                () -> configVersion,
                configVersion::equals,
                configVersion::equals
        );
        syncChannel.registerMessage(
                0,
                messageType,
                encoder,
                decoder,
                messageConsumer
        );
        this.messageSupplier = messageSupplier;
        MinecraftForge.EVENT_BUS.addListener(this::syncToPlayer);
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
        syncChannel.send(PacketDistributor.ALL.noArg(), this.messageSupplier.apply(getSyncConfig()));
    }

    private void syncToPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player == null) return;
        Copycats.LOGGER.debug("Sync Config: Sending server config to " + player.getScoreboardName());
        syncChannel.send(PacketDistributor.PLAYER.with(() -> player), this.messageSupplier.apply(getSyncConfig()));
    }

    public abstract static class SyncConfig {

        private final CompoundTag nbt;

        protected SyncConfig(CompoundTag nbt) {
            this.nbt = nbt;
        }

        protected abstract SyncConfigBase configInstance();

        void encode(FriendlyByteBuf buf) {
            buf.writeNbt(nbt);
        }

        static CompoundTag decode(FriendlyByteBuf buf) {
            return buf.readAnySizeNbt();
        }

        void handle(Supplier<Context> context) {
            Context ctx = context.get();
            ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                configInstance().setSyncConfig(nbt);
                Copycats.LOGGER.debug("Sync Config: Received and applied server config " + nbt.toString());
            }));
            ctx.setPacketHandled(true);
        }
    }

}
