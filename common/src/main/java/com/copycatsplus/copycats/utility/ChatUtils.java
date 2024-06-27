package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.config.CCConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

public class ChatUtils {
    public static Set<String> messages = new HashSet<>();

    public static void sendWarningOnce(String id, String message) {
        if (Minecraft.getInstance().player == null) return;
        if (CCConfigs.client().disableGraphicsWarnings.get()) return;
        if (messages.contains(id)) return;
        messages.add(id);
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Warning: " + message));
    }
}
