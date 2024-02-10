package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGen {

    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGen::genBlockTags);
//        Copycats.getRegistrate().addDataGenerator(CCDataProviderTypes.CAT_VARIANT_TAGS, CCTagGen::genCatVariantTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);
        prov.tag(TagKey.create(BuiltInRegistries.BLOCK.key(), Mods.DIAGONAL_FENCES.rl("non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get())
                .add(CCBlocks.WRAPPED_COPYCAT_FENCE.get());
    }

    // fabric: Not possible right now due to ProviderType not allowing third-party types
//    private static void genCatVariantTags(RegistrateTagsProvider<CatVariant> provIn) {
//        TagGen.CreateTagsProvider<CatVariant> prov = new TagGen.CreateTagsProvider<>(provIn, variant -> BuiltInRegistries.CAT_VARIANT.getResourceKey(variant).flatMap(BuiltInRegistries.CAT_VARIANT::getHolder).orElse(null));
//        prov.tag(CatVariantTags.FULL_MOON_SPAWNS)
//                .add(CCCatVariants.COPY_CAT.key());
//    }
}
