package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.compat.forge.AdditionalPlacementsCompatForge;
import com.copycatsplus.copycats.datagen.forge.CCDatagenImpl;
import com.copycatsplus.copycats.datagen.recipes.forge.CCCraftingConditions;
import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.utility.LogicalSidedProvider;
import com.copycatsplus.copycats.utility.Platform;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Copycats.MODID)
public class CopycatsImpl {

    static IEventBus bus;

    public CopycatsImpl() {
        bus = FMLJavaModLoadingContext.get().getModEventBus();
        Copycats.init();
        CCCreativeTabsImpl.register(CopycatsImpl.bus);

        CCCraftingConditions.register();
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(CopycatsImpl::onChunkUnload);
        MinecraftForge.EVENT_BUS.addListener(CopycatsImpl::onLevelUnload);

        Platform.Environment.CLIENT.runIfCurrent(() -> CopycatsClientImpl::init);
        bus.addListener(EventPriority.LOWEST, CCDatagenImpl::gatherData);
        Mods.ADDITIONAL_PLACEMENTS.executeIfInstalled(() -> AdditionalPlacementsCompatForge::register);
    }

    private void serverStarting(ServerStartingEvent event) {
        LogicalSidedProvider.setServer(event::getServer);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }

    static void onChunkUnload(ChunkEvent.Unload event) {
        CopycatMaterialStore.unloadChunk(event.getLevel(), event.getChunk().getPos());
    }

    static void onLevelUnload(LevelEvent.Unload event) {
        CopycatMaterialStore.unloadLevel(event.getLevel());
    }
}