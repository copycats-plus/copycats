package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.fabric.CCCraftingConditions;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class CopycatsImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        CCCreativeTabsImpl.register();
        Copycats.init();

        CCCraftingConditions.register();
        ServerLifecycleEvents.SERVER_STARTING.register(this::serverStarting);
    }

    private void serverStarting(MinecraftServer server) {
        LogicalSidedProvider.setServer(() -> server);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().register();
    }
}