package com.copycatsplus.copycats.utility;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * This class should only be accessed on the client side.
 * It is not marked as client-only itself to avoid class loading issues.
 */
public class ClientUtils {
    public static boolean sendSystemMessage(String message) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }
        Minecraft.getInstance().player.sendMessage(new TextComponent(message), Util.NIL_UUID);
        return true;
    }
}
