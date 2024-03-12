package com.copycatsplus.copycats.datagen.recipes.gen;

import com.copycatsplus.copycats.Copycats;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder.GeneratedRecipe;

public abstract class CopycatsRecipeProvider extends RecipeProvider {

    protected static final List<GeneratedRecipe> all = new ArrayList<>();

    public CopycatsRecipeProvider(PackOutput output) {
        super(output);
    }

    public void registerRecipes(@NotNull Consumer<FinishedRecipe> p_200404_1_) {
        all.forEach(c -> c.register(p_200404_1_));
        Copycats.LOGGER.info(getName() + " registered " + all.size() + " recipe" + (all.size() == 1 ? "" : "s"));
    }

    public static GeneratedRecipe register(GeneratedRecipe recipe) {
        all.add(recipe);
        return recipe;
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> writer) {
        all.forEach(recipe -> recipe.register(writer));
    }

}
