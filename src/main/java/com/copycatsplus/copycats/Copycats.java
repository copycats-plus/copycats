package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Copycats implements ModInitializer {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "copycats";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    @Override
    public void onInitialize() {
        CCCreativeTabs.register();

        // Register the commonSetup method for mod loading
        CCCraftingConditions.register();

        REGISTRATE.creativeModeTab(() -> CCCreativeTabs.MAIN);
        CCBlocks.register();
        CCItems.register();
        CCBlockEntityTypes.register();

        REGISTRATE.register();

        CCConfigs.register();
        CCConfigs.common().register();
    }

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
