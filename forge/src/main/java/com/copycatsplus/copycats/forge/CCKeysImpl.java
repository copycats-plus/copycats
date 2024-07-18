package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class CCKeysImpl {

    public static void register() {
        for (CCKeys key : CCKeys.values()) {
            key.keybind = new KeyMapping(key.description, key.key, Copycats.NAME);
            if (!key.modifiable)
                continue;
            ClientRegistry.registerKeyBinding(key.keybind);
        }
    }
}
