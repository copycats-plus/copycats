package com.copycatsplus.copycats;

public class CCDataProviderTypes {
    // fabric: not possible due to a bug in registrate fabric
    // https://github.com/Fabricators-of-Create/Registrate-Refabricated/pull/5
//    public static final ProviderType<RegistrateTagsProvider.IntrinsicImpl<CatVariant>> CAT_VARIANT_TAGS = ProviderType.register("tags/cat_variant",
//            type -> (p, e) -> new RegistrateTagsProvider.IntrinsicImpl<>(p,
//                    type,
//                    "cat_variant",
//                    e.getGenerator().getPackOutput(),
//                    Registries.CAT_VARIANT,
//                    e.getLookupProvider(),
//                    variant -> BuiltInRegistries.CAT_VARIANT.getResourceKey(variant).orElse(null),
//                    e.getExistingFileHelper()
//            )
//    );

    public static void register() {

    }
}
