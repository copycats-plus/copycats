package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCKeysImpl {

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        for (CCKeys key : CCKeys.values()) {
            key.keybind = new KeyMapping(key.description, key.key, Copycats.NAME);
            if (!key.modifiable)
                continue;
            event.register(key.keybind);
        }
    }
}
