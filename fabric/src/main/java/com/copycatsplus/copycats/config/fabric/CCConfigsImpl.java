package com.copycatsplus.copycats.config.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.config.CClient;
import com.copycatsplus.copycats.config.CCommon;
import com.copycatsplus.copycats.config.SyncConfigBase;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

public class CCConfigsImpl extends CCConfigs {

    public static void onLoad(ModConfig modConfig) {
        if (modConfig.getModId().equalsIgnoreCase(Copycats.MODID)) {
            for (ConfigBase config : CONFIGS.values())
                if (config.specification == modConfig
                        .getSpec())
                    config.onLoad();
        }
    }

    public static void onReload(ModConfig modConfig) {
        if (modConfig.getModId().equalsIgnoreCase(Copycats.MODID)) {
            for (ConfigBase config : CONFIGS.values())
                if (config.specification == modConfig
                        .getSpec())
                    config.onReload();
        }
    }

    public static void register() {
        client = register(CClient::new, ModConfig.Type.CLIENT);
        common = register(CCommon::new, ModConfig.Type.COMMON);

        for (Map.Entry<ModConfig.Type, SyncConfigBase> pair : CONFIGS.entrySet())
            ModLoadingContext.registerConfig(Copycats.MODID, pair.getKey(), pair.getValue().specification);

        ModConfigEvent.LOADING.register(CCConfigsImpl::onLoad);
        ModConfigEvent.RELOADING.register(CCConfigsImpl::onReload);
    }
}
