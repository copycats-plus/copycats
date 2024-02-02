package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGen {
    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGen::genBlockTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> prov) {
        prov.tag(TagKey.create(Registry.BLOCK.key(), Mods.DIAGONAL_FENCES.rl("non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get())
                .add(CCBlocks.WRAPPED_COPYCAT_FENCE.get());
    }
}
