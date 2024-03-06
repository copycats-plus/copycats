package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.multiloader.Platform;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.config.ModConfig;

public record ConfigSyncPacket(CompoundTag config, ModConfig.Type type) implements PacketSystem.S2CPacket {

    public ConfigSyncPacket(FriendlyByteBuf buf) {
        this(buf.readAnySizeNbt(), buf.readEnum(ModConfig.Type.class));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(config);
        buffer.writeEnum(type);
    }

    @Override
    public void handle(Minecraft mc) {
        Platform.Environment.CLIENT.runIfCurrent(() -> () -> {
            CCConfigs.byType(type()).setSyncConfig(config);
            Copycats.LOGGER.debug("Sync Config: Received and applied server config " + config.toString());
        });
    }
}
