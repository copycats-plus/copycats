package com.copycatsplus.copycats.datagen.recipes.forge;

import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CCStandardRecipesImpl {


    public static GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilderForge("/", result);
    }

    public static GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilderForge("/", result);
    }

    public static RecipeProvider create(DataGenerator generator) {
        CopycatsRecipeProvider provider = new CCStandardRecipes(generator);
        return new RecipeProvider(generator) {

            @Override
            public String getName() {
                return "Create: Copycats+ Recipe Provider";
            }

            @Override
            protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
                provider.registerRecipes(consumer);
            }
        };
    }
}

