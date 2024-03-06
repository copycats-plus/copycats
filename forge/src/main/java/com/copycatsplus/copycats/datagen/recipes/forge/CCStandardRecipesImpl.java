package com.copycatsplus.copycats.datagen.recipes.forge;

import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class CCStandardRecipesImpl extends CCStandardRecipes {
    protected CCStandardRecipesImpl(PackOutput output) {
        super(output);
    }

    public static RecipeProvider create(PackOutput output) {
        CCStandardRecipes recipes = new CCStandardRecipesImpl(output);
        return new RecipeProvider(output) {
            @Override
            protected void buildRecipes(Consumer<FinishedRecipe> writer) {
                recipes.registerRecipes(writer);
            }
        };
    }
}
