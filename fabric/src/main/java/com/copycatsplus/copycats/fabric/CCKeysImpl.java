package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class CCKeysImpl {

    public static void register() {
        for (CCKeys key : CCKeys.values()) {
            if (!key.modifiable)
                continue;
            key.keybind = new KeyMapping(key.description, key.key, Copycats.NAME);
            KeyBindingHelper.registerKeyBinding(key.keybind);
        }
        ClientTickEvents.END_CLIENT_TICK.register(client -> fixBinds());
    }

    // fabric: from create: sometimes after opening the toolbox menu, alt gets stuck as pressed until a screen is opened.
    // why is this needed? why did this only just now break? Good questions! I wish I knew.
    public static void fixBinds() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        for (CCKeys key : CCKeys.values()) {
            if (key.keybind == null || key.keybind.isUnbound())
                continue;
            key.keybind.setDown(InputConstants.isKeyDown(window, key.getBoundCode()));
        }
    }
}
