package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureCategory;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock;
import com.copycatsplus.copycats.foundation.copycat.WrappedCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamBlock;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamModelCore;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockBlock;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockModelCore;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.board.CopycatMultiBoardModelCore;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonModelCore;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatMultiByteModelCore;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorModelCore;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceModelCore;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeModelCore;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlock;
import com.copycatsplus.copycats.content.copycat.ghost_block.CopycatGhostBlockModelCore;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatMultiHalfLayerModelCore;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelModelCore;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderModelCore;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerModelCore;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatPressurePlateBlock;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatPressurePlateModelCore;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatWeightedPressurePlate;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.content.copycat.slab.CopycatMultiSlabModelCore;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceModelCore;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeBlock;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.slope_layer.CopycatSlopeLayerBlock;
import com.copycatsplus.copycats.content.copycat.slope_layer.CopycatSlopeLayerModelCore;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModelCore;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_slope.CopycatVerticalSlopeBlock;
import com.copycatsplus.copycats.content.copycat.vertical_slope.CopycatVerticalSlopeModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairsModelCore;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepBlock;
import com.copycatsplus.copycats.content.copycat.vertical_step.CopycatVerticalStepModelCore;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallModelCore;
import com.copycatsplus.copycats.datagen.CCBlockStateGen;
import com.copycatsplus.copycats.datagen.CCLootGen;
import com.copycatsplus.copycats.foundation.tooltip.CopycatCharacteristics;
import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.foundation.data.CreateRegistrate.blockModel;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("unused")
//noinspection unchecked
public class CCBlocks {

    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final BlockEntry<WrappedCopycatBlock> WRAPPED_COPYCAT =
            REGISTRATE.block("wrapped_copycat", WrappedCopycatBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .register();

    public static final BlockEntry<CopycatBaseBlock> COPYCAT_BASE =
            REGISTRATE.block("copycat_base", CopycatBaseBlock::new)
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.mapColor(MapColor.GLOW_LICHEN).noOcclusion())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
                    .transform(pickaxeOnly())
                    .blockstate(CCBlockStateGen::base)
                    .register();

    public static final BlockEntry<CopycatBlockBlock> COPYCAT_BLOCK =
            REGISTRATE.block("copycat_block", CopycatBlockBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatBlockModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.COPY_CAT
                    ))
                    .tag(CCTags.Items.COPYCAT_BLOCK.tag)
                    .transform(customItemModel("copycat_base", "block"))
                    .register();

    public static final BlockEntry<CopycatBeamBlock> COPYCAT_BEAM =
            REGISTRATE.block("copycat_beam", CopycatBeamBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatBeamModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .tag(CCTags.Items.COPYCAT_BEAM.tag)
                    .transform(customItemModel("copycat_base", "beam"))
                    .register();

    public static final BlockEntry<CopycatBoardBlock> COPYCAT_BOARD =
            REGISTRATE.block("copycat_board", CopycatBoardBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES))
                    .onRegister(createBlockModel(new CopycatMultiBoardModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForDirections()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.MULTI_STATE
                    ))
                    .tag(CCTags.Items.COPYCAT_BOARD.tag)
                    .transform(customItemModel("copycat_base", "board"))
                    .register();

    public static final BlockEntry<CopycatButtonBlock> COPYCAT_WOODEN_BUTTON =
            REGISTRATE.block("copycat_wooden_button", p -> new CopycatButtonBlock(p, BlockSetType.OAK, 30, true))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatButtonModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<CopycatButtonBlock> COPYCAT_STONE_BUTTON =
            REGISTRATE.block("copycat_stone_button", p -> new CopycatButtonBlock(p, BlockSetType.STONE, 20, false))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.STONE_BUTTONS)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatButtonModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<CopycatByteBlock> COPYCAT_BYTE =
            REGISTRATE.block("copycat_byte", CopycatByteBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES))
                    .onRegister(createBlockModel(new CopycatMultiByteModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForBytes()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.MULTI_STATE
                    ))
                    .transform(customItemModel("copycat_base", "byte"))
                    .register();

    public static final BlockEntry<CopycatFenceBlock> COPYCAT_FENCE =
            REGISTRATE.block("copycat_fence", CopycatFenceBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .tag(BlockTags.FENCES, CCTags.commonBlockTag("fences"))
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatFenceModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .tag(CCTags.Items.COPYCAT_FENCE.tag)
                    .transform(customItemModel("copycat_base", "fence"))
                    .register();

    public static final BlockEntry<CopycatFenceGateBlock> COPYCAT_FENCE_GATE =
            REGISTRATE.block("copycat_fence_gate", CopycatFenceGateBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .properties(BlockBehaviour.Properties::forceSolidOn)
                    .tag(BlockTags.FENCE_GATES, CCTags.commonBlockTag("fence_gates"), BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatFenceGateModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .tag(CCTags.Items.COPYCAT_FENCE_GATE.tag)
                    .transform(customItemModel("copycat_base", "fence_gate"))
                    .register();

    public static final BlockEntry<CopycatGhostBlock> COPYCAT_GHOST_BLOCK =
            REGISTRATE.block("copycat_ghost_block", CopycatGhostBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatGhostBlockModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.GHOST
                    ))
                    .transform(customItemModel("copycat_base", "ghost_block"))
                    .register();

    public static final BlockEntry<CopycatHalfLayerBlock> COPYCAT_HALF_LAYER =
            REGISTRATE.block("copycat_half_layer", CopycatHalfLayerBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.STACKABLES))
                    .onRegister(createBlockModel(new CopycatMultiHalfLayerModelCore()))
                    .loot(CCLootGen.build(
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.NEGATIVE_LAYERS),
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.POSITIVE_LAYERS)
                    ))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.MULTI_STATE,
                            CopycatCharacteristics.STACKABLE
                    ))
                    .transform(customItemModel("copycat_base", "half_layer"))
                    .register();

    public static final BlockEntry<CopycatHalfPanelBlock> COPYCAT_HALF_PANEL =
            REGISTRATE.block("copycat_half_panel", CopycatHalfPanelBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatHalfPanelModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .transform(customItemModel("copycat_base", "half_panel"))
                    .register();

    public static final BlockEntry<CopycatLadderBlock> COPYCAT_LADDER =
            REGISTRATE.block("copycat_ladder", CopycatLadderBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.CLIMBABLE)
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatLadderModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "ladder"))
                    .register();

    public static final BlockEntry<CopycatLayerBlock> COPYCAT_LAYER =
            REGISTRATE.block("copycat_layer", CopycatLayerBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.STACKABLES))
                    .onRegister(createBlockModel(new CopycatLayerModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.STACKABLE
                    ))
                    .transform(customItemModel("copycat_base", "layer"))
                    .register();

    public static final BlockEntry<CopycatPressurePlateBlock> COPYCAT_WOODEN_PRESSURE_PLATE =
            REGISTRATE.block("copycat_wooden_pressure_plate", p -> new CopycatPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p, BlockSetType.OAK))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatPressurePlateModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatPressurePlateBlock> COPYCAT_STONE_PRESSURE_PLATE =
            REGISTRATE.block("copycat_stone_pressure_plate", p -> new CopycatPressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p, BlockSetType.STONE))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.STONE_PRESSURE_PLATES)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatPressurePlateModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatWeightedPressurePlate> COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_heavy_weighted_pressure_plate", p -> new CopycatWeightedPressurePlate(150, p, BlockSetType.IRON))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatPressurePlateModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatWeightedPressurePlate> COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_light_weighted_pressure_plate", p -> new CopycatWeightedPressurePlate(15, p, BlockSetType.GOLD))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false)
                            .noCollission())
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register(FeatureCategory.REDSTONE, FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatPressurePlateModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<CopycatSlabBlock> COPYCAT_SLAB =
            REGISTRATE.block("copycat_slab", CopycatSlabBlock::new)
                    .transform(CCBuilderTransformers.multiCopycat())
                    .tag(BlockTags.SLABS)
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES))
                    .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                    .onRegister(blockModel(() -> model -> CopycatModelCore.createModel(model, new CopycatMultiSlabModelCore())))
                    .item()
                    .tag(CCTags.Items.COPYCAT_SLAB.tag)
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.MULTI_STATE
                    ))
                    .transform(customItemModel("copycat_base", "slab"))
                    .register();

    public static final BlockEntry<CopycatSliceBlock> COPYCAT_SLICE =
            REGISTRATE.block("copycat_slice", CopycatSliceBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.STACKABLES))
                    .onRegister(createBlockModel(new CopycatSliceModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.STACKABLE
                    ))
                    .transform(customItemModel("copycat_base", "slice"))
                    .register();

    public static final BlockEntry<CopycatStairsBlock> COPYCAT_STAIRS =
            REGISTRATE.block("copycat_stairs", CopycatStairsBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .tag(BlockTags.STAIRS)
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatStairsModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .tag(CCTags.Items.COPYCAT_STAIRS.tag)
                    .transform(customItemModel("copycat_base", "stairs"))
                    .register();

    public static final BlockEntry<CopycatVerticalStairBlock> COPYCAT_VERTICAL_STAIRS =
            REGISTRATE.block("copycat_vertical_stairs", CopycatVerticalStairBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .tag(BlockTags.STAIRS)
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatVerticalStairsModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .tag(CCTags.Items.COPYCAT_STAIRS.tag)
                    .transform(customItemModel("copycat_base", "vertical_stairs"))
                    .register();

    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_TRAPDOOR =
            REGISTRATE.block("copycat_trapdoor", p -> new CopycatTrapdoorBlock(p, BlockSetType.OAK))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.TRAPDOORS)
                    .tag(BlockTags.WOODEN_TRAPDOORS)
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatTrapdoorModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "trapdoor"))
                    .register();

    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_IRON_TRAPDOOR =
            REGISTRATE.block("copycat_iron_trapdoor", p -> new CopycatTrapdoorBlock(p, BlockSetType.IRON))
                    .transform(CCBuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.TRAPDOORS)
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatTrapdoorModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "trapdoor"))
                    .register();

    public static final BlockEntry<CopycatVerticalSliceBlock> COPYCAT_VERTICAL_SLICE =
            REGISTRATE.block("copycat_vertical_slice", CopycatVerticalSliceBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.STACKABLES))
                    .onRegister(createBlockModel(new CopycatVerticalSliceModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.STACKABLE
                    ))
                    .transform(customItemModel("copycat_base", "vertical_slice"))
                    .register();

    public static final BlockEntry<CopycatVerticalStepBlock> COPYCAT_VERTICAL_STEP =
            REGISTRATE.block("copycat_vertical_step", CopycatVerticalStepBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatVerticalStepModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .tag(CCTags.Items.COPYCAT_VERTICAL_STEP.tag)
                    .transform(customItemModel("copycat_base", "vertical_step"))
                    .register();

    public static final BlockEntry<CopycatWallBlock> COPYCAT_WALL =
            REGISTRATE.block("copycat_wall", CopycatWallBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .properties(BlockBehaviour.Properties::forceSolidOn)
                    .tag(BlockTags.WALLS)
                    .transform(FeatureToggle.register())
                    .onRegister(createBlockModel(new CopycatWallModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT
                    ))
                    .tag(CCTags.Items.COPYCAT_WALL.tag)
                    .transform(customItemModel("copycat_base", "wall"))
                    .register();

    public static final BlockEntry<CopycatSlopeBlock> COPYCAT_SLOPE =
            REGISTRATE.block("copycat_slope", CopycatSlopeBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.SLOPES))
                    .onRegister(createBlockModel(new CopycatSlopeModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .transform(customItemModel("copycat_base", "slope"))
                    .register();

    public static final BlockEntry<CopycatVerticalSlopeBlock> COPYCAT_VERTICAL_SLOPE =
            REGISTRATE.block("copycat_vertical_slope", CopycatVerticalSlopeBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.SLOPES))
                    .onRegister(createBlockModel(new CopycatVerticalSlopeModelCore()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE
                    ))
                    .transform(customItemModel("copycat_base", "vertical_slope"))
                    .register();

    public static final BlockEntry<CopycatSlopeLayerBlock> COPYCAT_SLOPE_LAYER =
            REGISTRATE.block("copycat_slope_layer", CopycatSlopeLayerBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.SLOPES, FeatureCategory.STACKABLES))
                    .onRegister(createBlockModel(new CopycatSlopeLayerModelCore()))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.CT_TOGGLE,
                            CopycatCharacteristics.STACKABLE
                    ))
                    .transform(customItemModel("copycat_base", "slope_layer"))
                    .register();

    public static final BlockEntry<CopycatShaftBlock> COPYCAT_SHAFT =
            REGISTRATE.block("copycat_shaft", CopycatShaftBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))
                    .transform(BlockStressDefaults.setNoImpact())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> CopycatModelCore.createModel(new BracketedKineticBlockModel(model), CopycatModelCore.PASS_THROUGH)))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "shaft"))
                    .register();

    public static final BlockEntry<CopycatCogWheelBlock> COPYCAT_COGWHEEL =
            REGISTRATE.block("copycat_cogwheel", CopycatCogWheelBlock::small)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))
                    .transform(BlockStressDefaults.setNoImpact())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> CopycatModelCore.createModel(new BracketedKineticBlockModel(model), CopycatModelCore.PASS_THROUGH)))
                    .item(CogwheelBlockItem::new)
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.MULTI_STATE,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "cogwheel"))
                    .register();

    public static final BlockEntry<CopycatCogWheelBlock> COPYCAT_LARGE_COGWHEEL =
            REGISTRATE.block("copycat_large_cogwheel", CopycatCogWheelBlock::large)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.MULTISTATES, FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))
                    .transform(BlockStressDefaults.setNoImpact())
                    .onRegister(CreateRegistrate.blockModel(() -> model -> CopycatModelCore.createModel(new BracketedKineticBlockModel(model), CopycatModelCore.PASS_THROUGH)))
                    .item(CogwheelBlockItem::new)
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.MULTI_STATE,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "large_cogwheel"))
                    .register();

    public static final BlockEntry<CopycatFluidPipeBlock> COPYCAT_FLUID_PIPE =
            REGISTRATE.block("copycat_fluid_pipe", CopycatFluidPipeBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL, FeatureCategory.CREATE))
                    .onRegister(CreateRegistrate.blockModel(() -> model -> getFluidPipeModel(model, new CopycatFluidPipeModelCore())))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "fluid_pipe"))
                    .register();

    public static final BlockEntry<CopycatGlassFluidPipeBlock> COPYCAT_GLASS_FLUID_PIPE =
            REGISTRATE.block("copycat_glass_fluid_pipe", CopycatGlassFluidPipeBlock::new)
                    .transform(CCBuilderTransformers.copycat())
                    .blockstate(CCBlockStateGen::glassPipe)
                    .onRegister(CreateRegistrate.blockModel(() -> model -> getFluidPipeModel(model, new CopycatStraightPipeModelCore())))
                    .loot((p, b) -> p.dropOther(b, COPYCAT_FLUID_PIPE.get()))
                    .register();

    public static final BlockEntry<CopycatDoorBlock> COPYCAT_DOOR =
            REGISTRATE.block("copycat_door", p -> new CopycatDoorBlock(p, BlockSetType.OAK))
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                    .onRegister(createBlockModel(new CopycatDoorModelCore()))
                    .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .tag(ItemTags.DOORS)
                    .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                    .transform(customItemModel("copycat_base", "door"))
                    .register();

    public static final BlockEntry<CopycatDoorBlock> COPYCAT_IRON_DOOR =
            REGISTRATE.block("copycat_iron_door", p -> new CopycatDoorBlock(p, BlockSetType.IRON))
                    .transform(CCBuilderTransformers.copycat())
                    .transform(FeatureToggle.register(FeatureCategory.FUNCTIONAL))
                    .onRegister(createBlockModel(new CopycatDoorModelCore()))
                    .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                    .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)))
                    .item()
                    .onRegister(CopycatDescription.register(
                            CopycatCharacteristics.COPYCAT,
                            CopycatCharacteristics.FUNCTIONAL
                    ))
                    .transform(customItemModel("copycat_base", "door"))
                    .register();

    @ExpectPlatform
    public static BakedModel getFluidPipeModel(BakedModel original, CopycatModelCore copycat) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        throw new AssertionError();
    }

    private static @NotNull <Model extends CopycatModelCore> NonNullConsumer<? super Block> createBlockModel(Model model) {
        return CreateRegistrate.blockModel(() -> original -> CopycatModelCore.createModel(original, model));
    }

    //Just in case we want/need it
    private static @NotNull <Model extends CopycatModelCore> NonNullConsumer<? super BlockItem> createItemModel(Model model) {
        return CreateRegistrate.itemModel(() -> original -> CopycatModelCore.createModel(original, model));
    }

    public static void register() {
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocks() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key()));
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredBlocksWithoutWrapped() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> !(entry.getId().getPath().startsWith("wrapped"))).collect(Collectors.toSet());
    }

    public static Set<RegistryEntry<Block>> getAllRegisteredMultiStateBlocks() {
        return new HashSet<>(REGISTRATE.getAll(BuiltInRegistries.BLOCK.key())).stream().filter(entry -> entry.get() instanceof IMultiStateCopycatBlock).collect(Collectors.toSet());
    }
}
