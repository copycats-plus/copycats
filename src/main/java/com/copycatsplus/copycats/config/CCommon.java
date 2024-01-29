package com.copycatsplus.copycats.config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class CCommon extends SyncConfigBase {
    private static final String VERSION = "1.0.0";

    @Override
    public String getName() {
        return "common";
    }

    public final CFeatures toggle = nested(0, CFeatures::new, Comments.toggle);

    public void register() {
        registerAsSyncRoot(
                VERSION,
                SyncConfig.class,
                SyncConfig::encode,
                SyncConfig::new,
                SyncConfig::handle,
                SyncConfig::new
        );
    }

    private static class Comments {
        static String toggle = "Enable/disable features. Values on server override clients";
    }

    private class SyncConfig extends SyncConfigBase.SyncConfig {

        protected SyncConfig(FriendlyByteBuf buf) {
            this(decode(buf));
        }

        protected SyncConfig(CompoundTag nbt) {
            super(nbt);
        }

        @Override
        protected SyncConfigBase configInstance() {
            return CCommon.this;
        }
    }
}
