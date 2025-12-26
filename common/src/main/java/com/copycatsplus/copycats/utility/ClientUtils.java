package com.copycatsplus.copycats.utility;

import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

/**
 * This class should only be accessed on the client side.
 * It is not marked as client-only itself to avoid class loading issues.
 */
public class ClientUtils {
    public static boolean sendSystemMessage(String message) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(message));
        return true;
    }

    public static boolean isVirtualRenderWorld(Level level) {
        return level instanceof VirtualRenderWorld;
    }
}
