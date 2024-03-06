package com.copycatsplus.copycats.datagen.recipes.fabric;

import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
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
        return new FabricRecipeProvider((FabricDataOutput) output) {
            @Override
            public void buildRecipes(Consumer<FinishedRecipe> writer) {
                recipes.registerRecipes(writer);
            }
        };
    }
}
