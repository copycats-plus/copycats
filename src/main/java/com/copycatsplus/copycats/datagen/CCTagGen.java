package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCCatVariants;
import com.copycatsplus.copycats.CCDataProviderTypes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class CCTagGen {

    public static void addGenerators() {
        Copycats.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, CCTagGen::genBlockTags);
        Copycats.getRegistrate().addDataGenerator(CCDataProviderTypes.CAT_VARIANT_TAGS, CCTagGen::genCatVariantTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> prov) {
        prov.tag(TagKey.create(ForgeRegistries.Keys.BLOCKS, Mods.DIAGONAL_FENCES.rl("non_diagonal_fences")))
                .add(CCBlocks.COPYCAT_FENCE.get())
                .add(CCBlocks.WRAPPED_COPYCAT_FENCE.get());
    }

    private static void genCatVariantTags(RegistrateTagsProvider<CatVariant> prov) {
        prov.tag(CatVariantTags.FULL_MOON_SPAWNS)
                .add(CCCatVariants.COPY_CAT.key());
    }
}
