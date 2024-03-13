package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.network.CCPackets;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Copycats {
	public static final String MODID = "copycats";
	public static final Logger LOGGER = LoggerFactory.getLogger("Copycats+");

	private static final CopycatRegistrate REGISTRATE = CopycatRegistrate.create(MODID);

	static {
		REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
				.andThen(TooltipModifier.mapNull(KineticStats.create(item))));
	}

	public static void init() {

		REGISTRATE.creativeModeTab(() -> CCCreativeTabs.MAIN);

		CCBlocks.register();
		CCBlockEntityTypes.register();
		CCItems.register();

		CCConfigs.register();

		CCPackets.PACKETS.registerC2SListener();

		finalizeRegistrate();
	}

	public static void gatherData(DataGenerator gen) {
		gen.addProvider(true, CCStandardRecipes.create(gen));
	}

	public static CopycatRegistrate getRegistrate() {
		return REGISTRATE;
	}

	public static ResourceLocation asResource(String path) {
		return new ResourceLocation(MODID, path);
	}

	@ExpectPlatform
	public static void finalizeRegistrate() {
		throw new AssertionError();
	}

}
