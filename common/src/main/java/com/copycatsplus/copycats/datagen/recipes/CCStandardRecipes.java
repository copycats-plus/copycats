package com.copycatsplus.copycats.datagen.recipes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class CCStandardRecipes extends CopycatsRecipeProvider {

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

    GeneratedRecipe COPYCAT_HALF_LAYER = copycat(CCBlocks.COPYCAT_HALF_LAYER, 16);

    GeneratedRecipe COPYCAT_WOODEN_BUTTON = copycat(CCBlocks.COPYCAT_WOODEN_BUTTON, 4);

    GeneratedRecipe COPYCAT_STONE_BUTTON = copycat(CCBlocks.COPYCAT_STONE_BUTTON, 4);

    GeneratedRecipe COPYCAT_WOOD_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE, 4);

    GeneratedRecipe COPYCAT_STONE_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_STONE_PRESSURE_PLATE, 4);

    GeneratedRecipe COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE, 2);

    GeneratedRecipe COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE = copycat(CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE, 2);


    String currentFolder = "";
    Marker enterFolder(String folder) {
        currentFolder = folder;
        return new Marker();
    }

    protected static class Marker {
    }

    GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new GeneratedRecipeBuilder("/", result);
    }

    GeneratedRecipeBuilder create(ResourceLocation result) {
        return new GeneratedRecipeBuilder("/", result);
    }

    GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    GeneratedRecipe copycat(ItemProviderEntry<? extends ItemLike> result, int resultCount) {
        if (result.get() instanceof CopycatBlock copycat) {
            copycatsWithRecipes.add(copycat);
        }
        return create(result)
                .unlockedBy(AllItems.ZINC_INGOT::get)
                .returns(resultCount)
                .viaStonecuttingTag(() -> CCTags.commonItemTag("ingots/zinc"))
                .create();
    }

    GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike>> cycle) {
        GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = cycle.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = cycle.get((i + 1) % cycle.size());
            result = create(nextEntry).withSuffix("_from_conversion")
                    .unlockedBy(currentEntry::get)
/*                    .requiresFeature(currentEntry.getId())
                    .requiresFeature(nextEntry.getId())*/
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    protected CCStandardRecipes(PackOutput output) {
        super(output);

        List<ResourceLocation> missingRecipes = new LinkedList<>();
        for (Map.Entry<ResourceKey<Block>, Block> entry : BuiltInRegistries.BLOCK.entrySet()) {
            if (entry.getKey().location().getNamespace().equals(Copycats.MODID) && entry.getValue() instanceof CopycatBlock) {
                if (!copycatsWithRecipes.contains(entry.getValue()))
                    missingRecipes.add(entry.getKey().location());
            }
        }
        if (!missingRecipes.isEmpty()) {
            throw new AssertionError("The following copycats do not have a crafting recipe: \n" + missingRecipes.stream().map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }

    @ExpectPlatform
    public static RecipeProvider create(PackOutput output) {
        throw new AssertionError();
    }

    @Override
    public String getName() {
        return "Create: Copycats+ Standard Recipes";
    }

    public static class GeneratedRecipeBuilder {

        private static String path = "";
        private static String suffix;
        private static Supplier<? extends ItemLike> result;
        private static ResourceLocation compatDatagenOutput;

        private Supplier<ItemPredicate> unlockedBy;
        private int amount;
        //Used by platforms;
        public static GeneratedRecipeBuilder instance;

        private GeneratedRecipeBuilder(String path) {
            GeneratedRecipeBuilder.path = path;
            suffix = "";
            this.amount = 1;
            instance = this;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            GeneratedRecipeBuilder.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            compatDatagenOutput = result;
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

        GeneratedRecipeBuilder withSuffix(String suffix) {
            GeneratedRecipeBuilder.suffix = suffix;
            return this;
        }


        GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                ShapedRecipeBuilder b = builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return handleConditions(consumer -> {
                ShapelessRecipeBuilder b = builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result.get(), amount));
                if (unlockedBy != null)
                    b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                b.save(consumer, createLocation("crafting"));
            });
        }

        private static ResourceLocation clean(ResourceLocation loc) {
            String path = loc.getPath();
            while (path.contains("//"))
                path = path.replaceAll("//", "/");
            return new ResourceLocation(loc.getNamespace(), path);
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + getRegistryName().getPath() + suffix));
            return loc;
        }

        protected static ResourceLocation createLocation(String recipeType) {
            ResourceLocation loc = clean(Copycats.asResource(recipeType + "/" + path + "/" + getRegistryName().getPath() + suffix));
            return loc;
        }

        GeneratedRecipeBuilder requiresResultFeature() {
            return requiresFeature(RegisteredObjects.getKeyOrThrow(result.get().asItem()));
        }

        GeneratedRecipeBuilder requiresFeature(ResourceLocation location) {
            return requiresFeature(location, false);
        }

        GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block) {
            return requiresFeature(block, false);
        }

        GeneratedRecipeBuilder requiresFeature(BlockEntry<?> block, boolean invert) {
            return requiresFeature(block.getId(), invert);
        }

        @ExpectPlatform
        public static GeneratedRecipeBuilder requiresFeature(ResourceLocation location, boolean invert) {
            throw new AssertionError();
        }

        @ExpectPlatform
        public static GeneratedRecipe handleConditions(Consumer<Consumer<FinishedRecipe>> recipe) {
            throw new AssertionError();
        }

        private static ResourceLocation getRegistryName() {
            return compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(result.get()
                    .asItem()) : compatDatagenOutput;
        }

        GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaCookingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedCookingRecipeBuilder(ingredient);
        }

        GeneratedStonecuttingRecipeBuilder viaStonecutting(Supplier<? extends ItemLike> item) {
            return unlockedBy(item).viaStonecuttingIngredient(() -> Ingredient.of(item.get()));
        }

        GeneratedStonecuttingRecipeBuilder viaStonecuttingTag(Supplier<TagKey<Item>> tag) {
            return unlockedByTag(tag).viaStonecuttingIngredient(() -> Ingredient.of(tag.get()));
        }

        GeneratedStonecuttingRecipeBuilder viaStonecuttingIngredient(Supplier<Ingredient> ingredient) {
            return new GeneratedStonecuttingRecipeBuilder(ingredient);
        }

        class GeneratedStonecuttingRecipeBuilder {

            private final Supplier<Ingredient> ingredient;

            GeneratedStonecuttingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
            }

            private GeneratedRecipe create(UnaryOperator<SingleItemRecipeBuilder> builder) {
                return handleConditions(consumer -> {
                    SingleItemRecipeBuilder b = builder.apply(SingleItemRecipeBuilder.stonecutting(ingredient.get(), RecipeCategory.MISC, result.get(), amount));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                    b.save(consumer, createLocation("stonecutting"));
                });
            }

            private GeneratedRecipe create() {
                return create(b -> b);
            }
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

            GeneratedCookingRecipeBuilder forDuration(int duration) {
                cookingTime = duration;
                return this;
            }

            GeneratedCookingRecipeBuilder rewardXP(float xp) {
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

                    SimpleCookingRecipeBuilder b = builder.apply(
                            SimpleCookingRecipeBuilder.generic(ingredient.get(), RecipeCategory.MISC, isOtherMod ? Items.DIRT : result.get(),
                                    exp, (int) (cookingTime * cookingTimeModifier), serializer));
                    if (unlockedBy != null)
                        b.unlockedBy("has_item", inventoryTrigger(unlockedBy.get()));
                    b.save(consumer::accept, createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer)
                            .getPath()));
                });
            }
        }
    }


}
