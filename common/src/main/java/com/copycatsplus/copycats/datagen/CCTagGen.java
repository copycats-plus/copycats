package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CCTagGen {

    @ExpectPlatform
    public static void addGenerators() {
        /*Copycats.getRegistrate().addDataGenerator(CCDataProviderTypes.CAT_VARIANT_TAGS, CCTagGen::genCatVariantTags);*/
    }

/*    private static void genCatVariantTags(RegistrateTagsProvider<CatVariant> provIn) {
        TagGen.CreateTagsProvider<CatVariant> prov = new TagGen.CreateTagsProvider<>(provIn, variant -> BuiltInRegistries.CAT_VARIANT.getResourceKey(variant).flatMap(BuiltInRegistries.CAT_VARIANT::getHolder).orElse(null));
        prov.tag(CatVariantTags.FULL_MOON_SPAWNS)
                .add(CCCatVariants.COPY_CAT.key());
    }*/
}
