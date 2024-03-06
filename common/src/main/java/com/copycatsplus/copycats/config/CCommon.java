package com.copycatsplus.copycats.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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

    public final CFeatures toggle = nested(0, CFeatures::new, Comments.toggle);

    public void register() {
    }

    private static class Comments {
        static String toggle = "Enable/disable features. Values on server override clients";
    }
}
