package com.copycatsplus.copycats.datagen.recipes.fabric;

import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class CCStandardRecipesImpl {


    public static GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilderFabric("/", result);
    }

    public static GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilderFabric("/", result);
    }
}
