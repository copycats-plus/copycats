package com.copycatsplus.copycats.config;

import com.simibubi.create.foundation.config.ConfigBase;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.config.ModConfig;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base class for all configs that require custom synchronization from server to clients.
 */
public abstract class SyncConfigBase extends ConfigBase {

    public final CompoundTag getSyncConfig() {
        CompoundTag nbt = new CompoundTag();
        writeSyncConfig(nbt);
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    if (nbt.contains(child.getName()))
                        throw new RuntimeException("A sync config key starts with " + child.getName() + " but does not belong to the child");
                    nbt.put(child.getName(), syncChild.getSyncConfig());
                }
            }
        return nbt;
    }

    protected abstract ModConfig.Type type();

    /**
     * Serialize all configs into the provided `nbt` tag to be synced to clients.
     *
     * @param nbt Accepts any value.
     */
    protected void writeSyncConfig(CompoundTag nbt) {
    }

    public final void setSyncConfig(CompoundTag config) {
        if (children != null)
            for (ConfigBase child : children) {
                if (child instanceof SyncConfigBase syncChild) {
                    CompoundTag nbt = config.getCompound(child.getName());
                    syncChild.readSyncConfig(nbt);
                }
            }
        readSyncConfig(config);
    }

    /**
     * Deserialize all configs from the provided `nbt` tag to a custom data storage.
     * The implementing class is responsible for data storage. No storage/config overwrite mechanism is provided here.
     *
     * @param nbt The configs sent from server.
     */
    protected void readSyncConfig(CompoundTag nbt) {
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onReload() {
        super.onReload();
    }

}
