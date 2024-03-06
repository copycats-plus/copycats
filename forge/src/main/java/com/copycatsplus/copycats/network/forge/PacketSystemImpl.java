package com.copycatsplus.copycats.network.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.network.PacketSystem;
import com.copycatsplus.copycats.network.PlayerSelection;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PacketSystemImpl extends PacketSystem {
    public static final Map<ResourceLocation, PacketSystem> HANDLERS = new HashMap<>();

    protected PacketSystemImpl(String id, int version,
                            List<Function<FriendlyByteBuf, S2CPacket>> s2cPackets,
                            Object2IntMap<Class<? extends S2CPacket>> s2cTypes,
                            List<Function<FriendlyByteBuf, C2SPacket>> c2sPackets,
                            Object2IntMap<Class<? extends C2SPacket>> c2sTypes) {
        super(id, version, s2cPackets, s2cTypes, c2sPackets, c2sTypes);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerS2CListener() {
        HANDLERS.put(s2cPacket, this);
    }

    @Override
    public void registerC2SListener() {
        HANDLERS.put(c2sPacket, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void send(SimplePacketBase packet) {
        AllPackets.getChannel().sendToServer(packet);
    }

    @Override
    public void sendTo(ServerPlayer player, SimplePacketBase packet) {
        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public void sendTo(PlayerSelection selection, SimplePacketBase packet) {
        AllPackets.getChannel().send(((PlayerSelectionImpl) selection).target, packet);
    }

    @Override
    protected void doSendC2S(FriendlyByteBuf buf) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(c2sPacket, buf));
        } else {
            Copycats.LOGGER.error("Cannot send a C2S packet before the client connection exists, skipping!");
        }
    }

    @Internal
    public static PacketSystem create(String id, int version,
                                   List<Function<FriendlyByteBuf, S2CPacket>> s2cPackets,
                                   Object2IntMap<Class<? extends S2CPacket>> s2cTypes,
                                   List<Function<FriendlyByteBuf, C2SPacket>> c2sPackets,
                                   Object2IntMap<Class<? extends C2SPacket>> c2sTypes) {
        return new PacketSystemImpl(id, version, s2cPackets, s2cTypes, c2sPackets, c2sTypes);
    }
}
