package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.CCKeys;
import com.copycatsplus.copycats.Copycats;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCKeysImpl {

    public static void register() {
        // no-op: registration is handled by the event subscriber
    }

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        for (CCKeys key : CCKeys.values()) {
            key.keybind = new KeyMapping(key.description, key.key, Copycats.NAME);
            if (!key.modifiable)
                continue;
            ClientRegistry.registerKeyBinding(key.keybind);
        }
    }
}
