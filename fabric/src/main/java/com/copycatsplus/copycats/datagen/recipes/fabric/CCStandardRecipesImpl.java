package com.copycatsplus.copycats.datagen.recipes.fabric;

import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CCStandardRecipesImpl {


    public static GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilderFabric("/", result);
    }

    public static GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilderFabric("/", result);
    }

    public static RecipeProvider create(DataGenerator generator) {
        CopycatsRecipeProvider provider = new CCStandardRecipes(generator);
        return new FabricRecipeProvider((FabricDataGenerator) generator) {

            @Override
            public String getName() {
                return "Create: Copycats+ Recipe Provider";
            }

            @Override
            protected void generateRecipes(Consumer<FinishedRecipe> exporter) {
                provider.registerRecipes(exporter);
            }
        };
    }
}
