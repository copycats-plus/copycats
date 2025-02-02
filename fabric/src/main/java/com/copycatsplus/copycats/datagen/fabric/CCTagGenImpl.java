package com.copycatsplus.copycats.datagen.fabric;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGenImpl {

    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGenImpl::genBlockTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        provIn.tag(TagKey.create(Registry.BLOCK.key(), new ResourceLocation(Mods.DIAGONAL_FENCES.id(), "non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get());
        provIn.tag(TagKey.create(Registry.BLOCK.key(), new ResourceLocation(Mods.DIAGONAL_WALLS.id(), "non_diagonal_walls")))
                .add(CCBlocks.COPYCAT_WALL.get());
    }
}
