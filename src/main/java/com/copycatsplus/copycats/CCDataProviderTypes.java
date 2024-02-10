package com.copycatsplus.copycats;

import com.copycatsplus.copycats.mixin.entity.DataInfoAccessor;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.level.material.Fluid;

public class CCDataProviderTypes {
    public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<CatVariant>> CAT_VARIANT_TAGS = ProviderType.register("tags/cat_variant",
            type -> (p, e) -> new RegistrateTagsProvider.IntrinsicImpl<>(p,
                    type,
                    "cat_variant",
                    e.getGenerator().getPackOutput(),
                    Registries.CAT_VARIANT,
                    e.getLookupProvider(),
                    variant -> BuiltInRegistries.CAT_VARIANT.getResourceKey(variant).orElse(null),
                    e.getExistingFileHelper()
            )
    );

    public static void register() {

    }
}
