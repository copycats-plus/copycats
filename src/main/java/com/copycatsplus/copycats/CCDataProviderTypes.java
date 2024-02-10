package com.copycatsplus.copycats;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.animal.CatVariant;

public class CCDataProviderTypes {
    public static final ProviderType<RegistrateTagsProvider<CatVariant>> CAT_VARIANT_TAGS = ProviderType.register("tags/cat_variant",
            type -> (p, e) -> new RegistrateTagsProvider<>(p,
                    type,
                    "cat_variant",
                    e.getGenerator(),
                    Registry.CAT_VARIANT,
                    e.getExistingFileHelper()
            )
    );

    public static void register() {

    }
}
