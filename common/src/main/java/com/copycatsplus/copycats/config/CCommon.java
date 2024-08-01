package com.copycatsplus.copycats.config;

import net.minecraftforge.fml.config.ModConfig;

public class CCommon extends SyncConfigBase {
    private static final String VERSION = "1.0.0";

    @Override
    protected ModConfig.Type type() {
        return ModConfig.Type.COMMON;
    }

    @Override
    public String getName() {
        return "common";
    }

    public final ConfigBool disableMigration = b(false, "disableMigration", Comments.disableMigration);

    public final CFeatures toggle = nested(0, CFeatures::new, Comments.toggle);

    public final CFeatureCategories categories = nested(0, CFeatureCategories::new, Comments.categories);

    public void register() {
    }

    private static class Comments {
        static String toggle = "Enable/disable features. Values on server override clients";
        static String categories = "Enable/disable categories of features. Disabling a category hides all related features. Values on server override clients";

        static String disableMigration = "Disables the migration of placed copycats from old versions to new ones. Setting this to true may cause copycats to lose their textures when you upgrade this mod. Restart the game to apply changes.";
    }
}
