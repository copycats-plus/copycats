package com.copycatsplus.copycats.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures all feature categories.
 * Values in this class should NOT be accessed directly. Please access via {@link FeatureToggle} instead.
 */
public class CFeatureCategories extends SyncConfigBase {

    @Override
    public String getName() {
        return "feature_categories";
    }

    final Map<FeatureCategory, ForgeConfigSpec.ConfigValue<Boolean>> toggles = new HashMap<>();

    Map<FeatureCategory, Boolean> synchronizedToggles;

    @Override
    public void registerAll(ForgeConfigSpec.Builder builder) {
        for (FeatureCategory r : FeatureCategory.values()) {
            builder.comment(".", r.getDescription());
            toggles.put(r, builder.define(r.getSerializedName(), true));
        }
    }

    @ApiStatus.Internal
    public boolean isEnabled(FeatureCategory category) {
        if (this.synchronizedToggles != null) {
            Boolean synced = synchronizedToggles.get(category);
            if (synced != null) return synced;
        }
        ForgeConfigSpec.ConfigValue<Boolean> value = toggles.get(category);
        if (value != null)
            return value.get();
        return true;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        FeatureToggle.refreshItemVisibility();
    }

    @Override
    public void onReload() {
        super.onReload();
        FeatureToggle.refreshItemVisibility();
    }

    @Override
    protected void readSyncConfig(CompoundTag nbt) {
        synchronizedToggles = new HashMap<>();
        for (String key : nbt.getAllKeys()) {
            FeatureCategory category = FeatureCategory.byName(key);
            synchronizedToggles.put(category, nbt.getBoolean(key));
        }
        FeatureToggle.refreshItemVisibility();
    }

    @Override
    protected ModConfig.Type type() {
        return ModConfig.Type.COMMON;
    }

    @Override
    protected void writeSyncConfig(CompoundTag nbt) {
        toggles.forEach((key, value) -> nbt.putBoolean(key.toString(), value.get()));
    }
}
