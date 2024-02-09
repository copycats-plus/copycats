package com.copycatsplus.copycats.datagen.recipes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.Copycats;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CCStandardRecipes extends CreateRecipeProvider {

    private final Set<CopycatBlock> copycatsWithRecipes = new HashSet<>();

    private final Marker PALETTES = enterFolder("palettes");

    GeneratedRecipe COPYCAT_SLAB = copycat(CCBlocks.COPYCAT_SLAB, 2);

    GeneratedRecipe COPYCAT_SLAB_FROM_PANELS = create(CCBlocks.COPYCAT_SLAB).withSuffix("_from_panels")
            .unlockedBy(AllBlocks.COPYCAT_PANEL::get)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('p', AllBlocks.COPYCAT_PANEL)
                    .pattern("p")
                    .pattern("p")
            );

    GeneratedRecipe COPYCAT_SLAB_FROM_STEPS = create(CCBlocks.COPYCAT_SLAB).withSuffix("_from_steps")
            .unlockedBy(AllBlocks.COPYCAT_STEP::get)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('s', AllBlocks.COPYCAT_STEP)
                    .pattern("ss")
            );

    GeneratedRecipe COPYCAT_SLAB_FROM_BEAMS = create(CCBlocks.COPYCAT_SLAB).withSuffix("_from_beams")
            .unlockedByTag(() -> CCTags.Items.COPYCAT_BEAM.tag)
            .requiresResultFeature()
            .requiresFeature(CCBlocks.COPYCAT_BEAM)
            .viaShaped(b -> b
                    .define('s', CCTags.Items.COPYCAT_BEAM.tag)
                    .pattern("ss")
            );

    GeneratedRecipe COPYCAT_BLOCK = copycat(CCBlocks.COPYCAT_BLOCK, 1);

    GeneratedRecipe COPYCAT_BLOCK_FROM_SLABS = create(CCBlocks.COPYCAT_BLOCK).withSuffix("_from_slabs")
            .unlockedByTag(() -> CCTags.Items.COPYCAT_SLAB.tag)
            .requiresResultFeature()
            .requiresFeature(CCBlocks.COPYCAT_SLAB)
            .viaShaped(b -> b
                    .define('s', CCTags.Items.COPYCAT_SLAB.tag)
                    .pattern("s")
                    .pattern("s")
            );

    GeneratedRecipe COPYCAT_BEAM = copycat(CCBlocks.COPYCAT_BEAM, 4);

    GeneratedRecipe COPYCAT_STEP_CYCLE_1 = create(AllBlocks.COPYCAT_STEP).withSuffix("_from_conversion")
            .unlockedByTag(() -> CCTags.Items.COPYCAT_VERTICAL_STEP.tag)
            .requiresFeature(CCBlocks.COPYCAT_VERTICAL_STEP)
            .viaShapeless(b -> b
                    .requires(CCTags.Items.COPYCAT_VERTICAL_STEP.tag)
            );

    GeneratedRecipe COPYCAT_STEP_CYCLE_2 = create(CCBlocks.COPYCAT_VERTICAL_STEP).withSuffix("_from_conversion")
            .unlockedBy(AllBlocks.COPYCAT_STEP::get)
            .requiresResultFeature()
            .viaShapeless(b -> b
                    .requires(AllBlocks.COPYCAT_STEP)
            );

    GeneratedRecipe COPYCAT_VERTICAL_STEP = copycat(CCBlocks.COPYCAT_VERTICAL_STEP, 4);

    GeneratedRecipe COPYCAT_HALF_PANEL = copycat(CCBlocks.COPYCAT_HALF_PANEL, 8);

    GeneratedRecipe COPYCAT_PANEL_FROM_HALF_PANELS = create(AllBlocks.COPYCAT_PANEL).withSuffix("_from_half_panels")
            .unlockedBy(CCBlocks.COPYCAT_HALF_PANEL::get)
            .requiresFeature(CCBlocks.COPYCAT_HALF_PANEL)
            .viaShaped(b -> b
                    .define('s', CCBlocks.COPYCAT_HALF_PANEL)
                    .pattern("ss")
            );

    GeneratedRecipe COPYCAT_STAIRS = copycat(CCBlocks.COPYCAT_STAIRS, 1);

    GeneratedRecipe COPYCAT_FENCE = copycat(CCBlocks.COPYCAT_FENCE, 1);

    GeneratedRecipe COPYCAT_FENCE_GATE = copycat(CCBlocks.COPYCAT_FENCE_GATE, 1);

    GeneratedRecipe COPYCAT_TRAPDOOR = copycat(CCBlocks.COPYCAT_TRAPDOOR, 4);

    GeneratedRecipe COPYCAT_TRAPDOOR_CYCLE =
            conversionCycle(ImmutableList.of(AllBlocks.COPYCAT_PANEL, CCBlocks.COPYCAT_TRAPDOOR));

    GeneratedRecipe COPYCAT_WALL = copycat(CCBlocks.COPYCAT_WALL, 1);

    GeneratedRecipe COPYCAT_BOARD = copycat(CCBlocks.COPYCAT_BOARD, 8);

    GeneratedRecipe COPYCAT_BOX = create(CCItems.COPYCAT_BOX)
            .unlockedByTag(() -> CCTags.Items.COPYCAT_BOARD.tag)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('s', CCTags.Items.COPYCAT_BOARD.tag)
                    .pattern("ss ")
                    .pattern("s s")
                    .pattern(" ss")
            );

    GeneratedRecipe COPYCAT_CATWALK = create(CCItems.COPYCAT_CATWALK)
            .unlockedByTag(() -> CCTags.Items.COPYCAT_BOARD.tag)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('s', CCTags.Items.COPYCAT_BOARD.tag)
                    .pattern("s s")
                    .pattern(" s ")
            );

    GeneratedRecipe COPYCAT_BYTE = copycat(CCBlocks.COPYCAT_BYTE, 8);

    GeneratedRecipe COPYCAT_LAYER = copycat(CCBlocks.COPYCAT_LAYER, 8);

    GeneratedRecipe COPYCAT_SLICE = copycat(CCBlocks.COPYCAT_SLICE, 16);

    GeneratedRecipe COPYCAT_VERTICAL_SLICE = copycat(CCBlocks.COPYCAT_VERTICAL_SLICE, 16);

    GeneratedRecipe COPYCAT_SLICE_CYCLE =
            conversionCycle(ImmutableList.of(CCBlocks.COPYCAT_SLICE, CCBlocks.COPYCAT_VERTICAL_SLICE));

    GeneratedRecipe COPYCAT_STEP_LAYER = copycat(CCBlocks.COPYCAT_STEP_LAYER, 16);

    GeneratedRecipe COPYCAT_WOODEN_BUTTON = copycat(CCBlocks.COPYCAT_WOODEN_BUTTON, 4);

    GeneratedRecipe COPYCAT_STONE_BUTTON = copycat(CCBlocks.COPYCAT_STONE_BUTTON, 4);

    GeneratedRecipe COPYCAT_WOOD_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE, 4);

    GeneratedRecipe COPYCAT_STONE_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_STONE_PRESSURE_PLATE, 4);

    GeneratedRecipe COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE, 2);

    GeneratedRecipe COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE, 2);

    String currentFolder = "";

    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder(currentFolder, result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipe createSpecial(Supplier<? extends SimpleCraftingRecipeSerializer<?>> serializer, String recipeType,
                                  String path) {
        ResourceLocation location = Copycats.asResource(recipeType + "/" + currentFolder + "/" + path);
        return register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special(serializer.get());
            b.save(consumer, location.toString());
        });
    }

    GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike>> cycle) {
        GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("_from_conversion")
                    .unlockedBy(currentEntry::get)
                    .requiresFeature(currentEntry.getId())
                    .requiresFeature(nextEntry.getId())
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    GeneratedRecipe copycat(ItemProviderEntry<? extends ItemLike> result, int resultCount) {
        if (result.get() instanceof CopycatBlock copycat) {
            copycatsWithRecipes.add(copycat);
        }
        return create(result)
                .unlockedBy(AllItems.ZINC_INGOT::get)
                .requiresResultFeature()
                .viaStonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/zinc")), resultCount);
    }

//    @Override
//    public @NotNull String getName() {
//        return "Standard Recipes of Create: Copycats+"; // Not really worth it to use access transformer on this insignificant change
//    }

    public CCStandardRecipes(PackOutput output) {
        super(output);

        List<ResourceLocation> missingRecipes = new LinkedList<>();
        for (Map.Entry<ResourceKey<Block>, Block> entry : ForgeRegistries.BLOCKS.getEntries()) {
            if (entry.getKey().location().getNamespace().equals(Copycats.MODID) && entry.getValue() instanceof CopycatBlock) {
                if (!copycatsWithRecipes.contains(entry.getValue()))
                    missingRecipes.add(entry.getKey().location());
            }
        }
        if (!missingRecipes.isEmpty()) {
            throw new IllegalStateException("The following copycats do not have a crafting recipe: " + missingRecipes.stream().map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }

    protected static class Marker {
    }


    class GeneratedRecipeBuilder {

        private final String path;
        private String suffix;
        private Supplier<? extends ItemLike> result;
        private RecipeCategory category = null;
        private ResourceLocation compatDatagenOutput;
        List<ICondition> recipeConditions;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList<>();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(item.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item()
                    .of(tag.get())
                    .build();
            return this;
        }

        GeneratedRecipeBuilder inCategory(RecipeCategory category) {
            this.category = category;
            return this;
        }

        GeneratedRecipeBuilder whenModLoaded(String modid) {
            return withCondition(new ModLoadedCondition(modid));
        }

        GeneratedRecipeBuilder whenModMissing(String modid) {
            return withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        GeneratedRecipeBuilder withCondition(ICondition condition) {
            recipeConditions.add(condition);
            return this;
        }

        GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert) {
            recipeConditions.add(new FeatureEnabledCondition(location, invert));
            return this;
        }

        GeneratedRecipeBuilder requiresFeature(ResourceLocation location) {
            return requiresFeature(location, false);
        }

        GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block, boolean invert) {
            return requiresFeature(block.getId(), invert);
        }

        GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block) {
            return requiresFeature(block, false);
        }

        GeneratedRecipeBuilder requiresResultFeature() {
            return requiresFeature(RegisteredObjects.getKeyOrThrow(result.get().asItem()));
        }

        GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        // FIXME 5.1 refactor - recipe categories as markers instead of sections?
        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(category == null ? RecipeCategory.MISC : category, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(category == null ? RecipeCategory.MISC : category, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaStonecutting(Ingredient ingredient, int resultCount) {
            return handleConditions(consumer -> {
                SingleItemRecipeBuilder b = SingleItemRecipeBuilder.stonecutting(ingredient, category == null ? RecipeCategory.BUILDING_BLOCKS : category, result.get(), resultCount);
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaStonecutting(Ingredient ingredient) {
            return viaStonecutting(ingredient, 1);
        }

        GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return handleConditions(consumer -> {
                SmithingTransformRecipeBuilder b =
                        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(base.get()), upgradeMaterial.get(), category == null ? RecipeCategory.COMBAT : category, result.get()
                                        .asItem());
                b.unlocks("has_item", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(base.get())
                        .build()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private GeneratedRecipe handleConditions(Consumer<Consumer<FinishedRecipe>> recipe) {
            return register(consumer -> {
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

        private ResourceLocation createSimpleLocation(String recipeType) {
            return Copycats.asResource(recipeType + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            return Copycats.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix);
        }

        private ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private final Supplier<Ingredient> ingredient;
            private float exp;
            private int cookingTime;

            private final RecipeSerializer<? extends AbstractCookingRecipe> FURNACE = RecipeSerializer.SMELTING_RECIPE,
                    SMOKER = RecipeSerializer.SMOKING_RECIPE, BLAST = RecipeSerializer.BLASTING_RECIPE,
                    CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                cookingTime = 200;
                exp = 0;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                exp = xp;
                return this;
            }

            GeneratedRecipe inFurnace() {
                return inFurnace(b -> b);
            }

            GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return create(FURNACE, builder, 1);
            }

            GeneratedRecipe inSmoker() {
                return inSmoker(b -> b);
            }

            GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                create(CAMPFIRE, builder, 3);
                return create(SMOKER, builder, .5f);
            }

            GeneratedRecipe inBlastFurnace() {
                return inBlastFurnace(b -> b);
            }

            GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                create(FURNACE, builder, 1);
                return create(BLAST, builder, .5f);
            }

            private GeneratedRecipe create(RecipeSerializer<? extends AbstractCookingRecipe> serializer,
                                           UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return register(consumer -> {
                    boolean isOtherMod = compatDatagenOutput != null;

                    SimpleCookingRecipeBuilder b = builder.apply(SimpleCookingRecipeBuilder.generic(ingredient.get(),
                            RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(), exp,
                            (int) (cookingTime * cookingTimeModifier), serializer));

                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));

                    b.save(result -> consumer.accept(
                            isOtherMod ? new ModdedCookingRecipeResult(result, compatDatagenOutput, recipeConditions)
                                    : result), createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer)
                            .getPath()));
                });
            }
        }
    }

    private static class ModdedCookingRecipeResult implements FinishedRecipe {

        private final FinishedRecipe wrapped;
        private final ResourceLocation outputOverride;
        private final List<ICondition> conditions;

        public ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride,
                                         List<ICondition> conditions) {
            this.wrapped = wrapped;
            this.outputOverride = outputOverride;
            this.conditions = conditions;
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return wrapped.getId();
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject object) {
            wrapped.serializeRecipeData(object);
            object.addProperty("result", outputOverride.toString());

            JsonArray conds = new JsonArray();
            conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }

    }
}
