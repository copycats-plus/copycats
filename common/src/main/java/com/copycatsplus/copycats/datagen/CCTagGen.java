package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGen {

    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGen::genBlockTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);
        prov.tag(TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation(Mods.DIAGONAL_FENCES.id(), "non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get());
        prov.tag(TagKey.create(BuiltInRegistries.BLOCK.key(), new ResourceLocation(Mods.DIAGONAL_WALLS.id(), "non_diagonal_walls")))
                .add(CCBlocks.COPYCAT_WALL.get());
//        prov.tag(AllTags.AllBlockTags.COPYCAT_DENY.tag)
//                .addTag(AllTags.AllBlockTags.TRACKS.tag);
    }
}
