package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.fabric.CCCraftingConditions;
import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;

public class CopycatsImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        CCCreativeTabsImpl.register();
        Copycats.init();

        CCCraftingConditions.register();
        ServerLifecycleEvents.SERVER_STARTING.register(this::serverStarting);
        ServerChunkEvents.CHUNK_UNLOAD.register(CopycatsImpl::onChunkUnload);
        ServerWorldEvents.UNLOAD.register(CopycatsImpl::onLevelUnload);
    }

    private void serverStarting(MinecraftServer server) {
        LogicalSidedProvider.setServer(() -> server);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().register();
    }

    static void onChunkUnload(LevelAccessor level, LevelChunk chunk) {
        CopycatMaterialStore.unloadChunk(level, chunk.getPos());
    }

    static void onLevelUnload(Object source, LevelAccessor level) {
        CopycatMaterialStore.unloadLevel(level);
    }
}