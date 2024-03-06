package com.copycatsplus.copycats.datagen.recipes.forge;

import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes.GeneratedRecipeBuilder;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CCStandardRecipesGeneratedRecipeBuilderImpl extends GeneratedRecipeBuilder {

    private static final List<ICondition> recipeConditions = new ArrayList<>();

    public CCStandardRecipesGeneratedRecipeBuilderImpl(String path, Supplier<? extends ItemLike> result) {
        super(path, result);
    }

    public static GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert) {
        recipeConditions.add(new FeatureEnabledCondition(location, invert));
        return instance;
    }


    public static CopycatsRecipeProvider.GeneratedRecipe handleConditions(Consumer<Consumer<FinishedRecipe>> recipe) {
        return CopycatsRecipeProvider.register(consumer -> {
            if (!recipeConditions.isEmpty()) {
                ConditionalRecipe.Builder b = ConditionalRecipe.builder();
                recipeConditions.forEach(b::addCondition);
                b.addRecipe(recipe);
                b.generateAdvancement();
                b.build(consumer, createLocation("crafting"));
            } else {
                recipe.accept(consumer);
            }
        });
    }

}
