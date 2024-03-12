package com.copycatsplus.copycats.datagen.recipes.gen;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface GeneratedRecipeBuilder {

    GeneratedRecipeBuilder returns(int amount);

    GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item);

    GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag);

    GeneratedRecipeBuilder withSuffix(String suffix);

    GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder);

    GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder);

    GeneratedRecipeBuilder requiresResultFeature();

    GeneratedRecipeBuilder requiresFeature(ResourceLocation location);

    GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block);

    GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block, boolean invert);

    GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert);

    GeneratedRecipe handleConditions(Consumer<Consumer<FinishedRecipe>> recipe);

    GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item);

    GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag);

    GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient);

    GeneratedStoneCuttingRecipeBuilder viaStonecutting(Supplier<? extends ItemLike> item);

    GeneratedStoneCuttingRecipeBuilder viaStonecuttingTag(Supplier<TagKey<Item>> tag);

    GeneratedStoneCuttingRecipeBuilder viaStonecuttingIngredient(Supplier<Ingredient> ingredient);

    public interface GeneratedCookingRecipeBuilder {

        GeneratedCookingRecipeBuilder forDuration(int duration);

        GeneratedCookingRecipeBuilder rewardXP(float xp);

        GeneratedRecipe inFurnace();

        GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder);

        GeneratedRecipe inSmoker();

        GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder);

        GeneratedRecipe inBlastFurnace();

        GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder);
    }

    public interface GeneratedStoneCuttingRecipeBuilder {

        GeneratedRecipe create();
    }

    @FunctionalInterface
    public interface GeneratedRecipe {

        void register(Consumer<FinishedRecipe> consumer);
    }
}
