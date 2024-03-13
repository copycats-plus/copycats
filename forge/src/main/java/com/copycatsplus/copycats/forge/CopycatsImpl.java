package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.datagen.forge.CCDatagenImpl;
import com.copycatsplus.copycats.datagen.recipes.forge.CCCraftingConditions;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.multiloader.Platform;
import net.minecraftforge.common.MinecraftForge;
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

        CCCraftingConditions.register();
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

        Platform.Environment.CLIENT.runIfCurrent(() -> CopycatsClient::init);
        bus.addListener(EventPriority.LOWEST, CCDatagenImpl::gatherData);
    }

    private void serverStarting(ServerStartingEvent event) {
        LogicalSidedProvider.setServer(event::getServer);
    }

    public static void finalizeRegistrate() {
        Copycats.getRegistrate().registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }
}