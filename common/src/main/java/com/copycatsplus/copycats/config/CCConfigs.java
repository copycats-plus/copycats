package com.copycatsplus.copycats.config;

import com.simibubi.create.foundation.config.ConfigBase;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CCConfigs {

    public static final Map<ModConfig.Type, SyncConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    protected static CClient client;
    protected static CCommon common;

    public static CClient client() {
        return client;
    }

    public static CCommon common() {
        return common;
    }

    public static <T> Supplier<T> safeGetter(Supplier<T> getter, T defaultValue) {
        return () -> {
            try {
                return getter.get();
            } catch (IllegalStateException | NullPointerException ex) {
                // the config is accessed too early (before registration or before config load)
                return defaultValue;
            }
        };
    }

    public static SyncConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    public static <T extends SyncConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    @ExpectPlatform
    public static void register() {
    }

}
