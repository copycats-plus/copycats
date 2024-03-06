package com.copycatsplus.copycats.config.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.config.CClient;
import com.copycatsplus.copycats.config.CCommon;
import com.copycatsplus.copycats.config.SyncConfigBase;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Copycats.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCConfigsImpl extends CCConfigs {

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onReload();
    }

    public static void register() {
        ModLoadingContext context = ModLoadingContext.get();
        client = register(CClient::new, ModConfig.Type.CLIENT);
        common = register(CCommon::new, ModConfig.Type.COMMON);

        for (Map.Entry<ModConfig.Type, SyncConfigBase> pair : CONFIGS.entrySet())
            context.registerConfig(pair.getKey(), pair.getValue().specification);
    }
}
