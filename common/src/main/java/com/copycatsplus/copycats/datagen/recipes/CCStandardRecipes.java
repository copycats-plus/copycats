package com.copycatsplus.copycats.datagen.recipes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCItems;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.gen.CopycatsRecipeProvider;
import com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.datagen.recipes.gen.GeneratedRecipeBuilder.GeneratedRecipe;

public class CCStandardRecipes extends CopycatsRecipeProvider {

    private final Set<CopycatBlock> copycatsWithRecipes = new HashSet<>();

    private final Marker PALETTES = enterFolder("palettes");

    GeneratedRecipe COPYCAT_SLAB = copycat(CCBlocks.COPYCAT_SLAB, 2);

    GeneratedRecipe COPYCAT_SLAB_FROM_PANELS = create(CCBlocks.COPYCAT_SLAB).withSuffix("_from_panels")
            .unlockedBy(AllBlocks.COPYCAT_PANEL::get)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('p', AllBlocks.COPYCAT_PANEL.get())
                    .pattern("p")
                    .pattern("p")
            );

    GeneratedRecipe COPYCAT_SLAB_FROM_STEPS = create(CCBlocks.COPYCAT_SLAB).withSuffix("_from_steps")
            .unlockedBy(AllBlocks.COPYCAT_STEP::get)
            .requiresResultFeature()
            .viaShaped(b -> b
                    .define('s', AllBlocks.COPYCAT_STEP.get())
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
                    .requires(AllBlocks.COPYCAT_STEP.get())
            );

    GeneratedRecipe COPYCAT_VERTICAL_STEP = copycat(CCBlocks.COPYCAT_VERTICAL_STEP, 4);

    GeneratedRecipe COPYCAT_HALF_PANEL = copycat(CCBlocks.COPYCAT_HALF_PANEL, 8);

    GeneratedRecipe COPYCAT_PANEL_FROM_HALF_PANELS = create(AllBlocks.COPYCAT_PANEL).withSuffix("_from_half_panels")
            .unlockedBy(CCBlocks.COPYCAT_HALF_PANEL::get)
            .requiresFeature(CCBlocks.COPYCAT_HALF_PANEL)
            .viaShaped(b -> b
                    .define('s', CCBlocks.COPYCAT_HALF_PANEL.get())
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

    @ExpectPlatform
    static GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        throw new AssertionError();
    }

    @ExpectPlatform
    static GeneratedRecipeBuilder create(ResourceLocation result) {
        throw new AssertionError();
    }

    static GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return create(result::get);
    }

    @ExpectPlatform
    public static RecipeProvider create(DataGenerator generator) {
        throw new AssertionError();
    }

    GeneratedRecipeBuilder.GeneratedRecipe copycat(ItemProviderEntry<? extends ItemLike> result, int resultCount) {
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
                    .requiresFeature(currentEntry.getId())
                    .requiresFeature(nextEntry.getId())
                    .viaShapeless(b -> b.requires(currentEntry.get()));
        }
        return result;
    }

    public CCStandardRecipes(DataGenerator output) {
        super(output);

        List<ResourceLocation> missingRecipes = new LinkedList<>();
        for (Map.Entry<ResourceKey<Block>, Block> entry : Registry.BLOCK.entrySet()) {
            if (entry.getKey().location().getNamespace().equals(Copycats.MODID) && entry.getValue() instanceof CopycatBlock copycatBlock) {
                if (!copycatsWithRecipes.contains(copycatBlock))
                    missingRecipes.add(entry.getKey().location());
            }
        }
        if (!missingRecipes.isEmpty()) {
            throw new AssertionError("The following copycats do not have a crafting recipe: \n" + missingRecipes.stream().map(ResourceLocation::toString).collect(Collectors.joining(", ")));
        }
    }

}
