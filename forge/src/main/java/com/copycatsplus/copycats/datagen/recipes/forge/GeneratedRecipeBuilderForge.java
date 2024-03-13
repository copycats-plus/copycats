package com.copycatsplus.copycats.datagen.recipes.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.inventoryTrigger;

public class GeneratedRecipeBuilderForge implements GeneratedRecipeBuilder {

    private final List<ICondition> recipeConditions = new ArrayList<>();
    private String path = "";
    private String suffix;
    private Supplier<? extends ItemLike> result;
    private ResourceLocation compatDatagenOutput;

    private Supplier<ItemPredicate> unlockedBy;
    private int amount;

    private GeneratedRecipeBuilderForge(String path) {
        this.path = path;
        suffix = "";
        this.amount = 1;
    }

    public GeneratedRecipeBuilderForge(String path, Supplier<? extends ItemLike> result) {
        this(path);
        this.result = result;
    }

    public GeneratedRecipeBuilderForge(String path, ResourceLocation result) {
        this(path);
        compatDatagenOutput = result;
    }

    @Override
    public GeneratedRecipeBuilder returns(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
        this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(item.get())
                .build();
        return this;
    }

    @Override
    public GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
        this.unlockedBy = () -> ItemPredicate.Builder.item()
                .of(tag.get())
                .build();
        return this;
    }

    @Override
    public GeneratedRecipeBuilder withSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
        return handleConditions(consumer -> {
            ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(result.get(), amount));
            if (unlockedBy != null)
                b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
            b.save(consumer, createLocation("crafting"));
        });
    }

    @Override
    public GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
        return handleConditions(consumer -> {
            ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(result.get(), amount));
            if (unlockedBy != null)
                b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
            b.save(consumer, createLocation("crafting"));
        });
    }

    private ResourceLocation clean(ResourceLocation loc) {
        String path = loc.getPath();
        while (path.contains("//"))
            path = path.replaceAll("//", "/");
        return new ResourceLocation(loc.getNamespace(), path);
    }

    private ResourceLocation createSimpleLocation(String recipeType) {
        ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + getRegistryName().getPath() + suffix));
        return loc;
    }

    protected ResourceLocation createLocation(String recipeType) {
        ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix));
        return loc;
    }

    private ResourceLocation getRegistryName() {
        return compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(result.get()
                .asItem()) : compatDatagenOutput;
    }


    @Override
    public GeneratedRecipeBuilder requiresResultFeature() {
        return requiresFeature(RegisteredObjects.getKeyOrThrow(result.get().asItem()));
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(ResourceLocation location) {
        return requiresFeature(location, false);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block) {
        return requiresFeature(block, false);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block, boolean invert) {
        return requiresFeature(block.getId(), invert);
    }

    @Override
    public GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert) {
        recipeConditions.add(new FeatureEnabledCondition(location, invert));
        return this;
    }

    @Override
    public GeneratedRecipe handleConditions(Consumer<Consumer<FinishedRecipe>> recipe) {
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

    @Override
    public GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
        return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
    }

    @Override
    public GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
        return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
    }

    @Override
    public GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
        return new GeneratedCookingRecipeBuilderForge(ingredient);
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecutting(Supplier<? extends ItemLike> item) {
        return unlockedBy(item).viaStonecuttingIngredient(() -> Ingredient.of(item.get()));
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecuttingTag(Supplier<TagKey<Item>> tag) {
        return unlockedByTag(tag).viaStonecuttingIngredient(() -> Ingredient.of(tag.get()));
    }

    @Override
    public GeneratedStoneCuttingRecipeBuilder viaStonecuttingIngredient(Supplier<Ingredient> ingredient) {
        return new GeneratedStoneCuttingRecipeBuilderForge(ingredient);
    }

    public class GeneratedCookingRecipeBuilderForge implements GeneratedCookingRecipeBuilder {

        private final Supplier<Ingredient> ingredient;
        private float exp;
        private int cookingTime;

        private final SimpleCookingSerializer<? extends AbstractCookingRecipe> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;

        GeneratedCookingRecipeBuilderForge(Supplier<Ingredient> ingredient) {
            this.ingredient = ingredient;
            cookingTime = 200;
            exp = 0;
        }

        @Override
        public GeneratedCookingRecipeBuilder forDuration(int duration) {
            cookingTime = duration;
            return this;
        }

        @Override
        public GeneratedCookingRecipeBuilder rewardXP(float xp) {
            exp = xp;
            return this;
        }

        @Override
        public GeneratedRecipe inFurnace() {
            return inFurnace(b -> b);
        }

        @Override
        public GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            return create(FURNACE, builder, 1);
        }

        @Override
        public GeneratedRecipe inSmoker() {
            return inSmoker(b -> b);
        }

        @Override
        public GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            create(FURNACE, builder, 1);
            create(CAMPFIRE, builder, 3);
            return create(SMOKER, builder, .5f);
        }

        @Override
        public GeneratedRecipe inBlastFurnace() {
            return inBlastFurnace(b -> b);
        }

        @Override
        public GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
            create(FURNACE, builder, 1);
            return create(BLAST, builder, .5f);
        }

        private GeneratedRecipe create(SimpleCookingSerializer<? extends AbstractCookingRecipe> serializer,
                                       UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
            return CopycatsRecipeProvider.register(consumer -> {
                boolean isOtherMod = compatDatagenOutput != null;

                SimpleCookingRecipeBuilder b = builder.apply(
                        SimpleCookingRecipeBuilder.cooking(ingredient.get(), isOtherMod ? Items.DIRT : result.get(),
                                exp, (int) (cookingTime * cookingTimeModifier), serializer));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer::accept, createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer)
                        .getPath()));
            });
        }
    }

    public class GeneratedStoneCuttingRecipeBuilderForge implements GeneratedStoneCuttingRecipeBuilder {

        private final Supplier<Ingredient> ingredient;

        GeneratedStoneCuttingRecipeBuilderForge(Supplier<Ingredient> ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public GeneratedRecipe create() {
            return create(b -> b);
        }

        private GeneratedRecipe create(UnaryOperator<SingleItemRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                SingleItemRecipeBuilder b = builder.apply(SingleItemRecipeBuilder.stonecutting(ingredient.get(), result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("stonecutting"));
            });
        }
    }
}

