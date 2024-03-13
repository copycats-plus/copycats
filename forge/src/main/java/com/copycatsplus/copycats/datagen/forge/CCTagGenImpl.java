package com.copycatsplus.copycats.datagen.forge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.datagen.CCTagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGenImpl {
    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGenImpl::genBlockTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        provIn.tag(TagKey.create(Registry.BLOCK.key(), Mods.DIAGONAL_FENCES.rl("non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get())
                .add(CCBlocks.WRAPPED_COPYCAT_FENCE.get());
    }
}
