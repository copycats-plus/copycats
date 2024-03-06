package com.copycatsplus.copycats.multiloader;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

import static com.copycatsplus.copycats.multiloader.NetworkUtil.NetworkDirection.*;

public class NetworkUtil {

    public enum NetworkDirection {
        PLAY_TO_CLIENT,
        PLAY_TO_SERVER
    }

    public record Context(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
        public void enqueueWork(Runnable runnable) {
            exec().execute(runnable);
        }

        @Nullable
        public ServerPlayer getSender() {
            return sender();
        }

        public NetworkDirection getDirection() {
            return sender() == null ? PLAY_TO_SERVER : PLAY_TO_CLIENT;
        }
    }
}
