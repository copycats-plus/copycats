package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.ToggleableCopycatModel;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamBlock;
import com.copycatsplus.copycats.content.copycat.beam.CopycatBeamModel;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockBlock;
import com.copycatsplus.copycats.content.copycat.block.CopycatBlockModel;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoardModel;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonModel;
import com.copycatsplus.copycats.content.copycat.button.CopycatStoneButtonBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatWoodenButtonBlock;
import com.copycatsplus.copycats.content.copycat.button.WrappedButton;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteModel;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceModel;
import com.copycatsplus.copycats.content.copycat.fence.WrappedFenceBlock;
import com.copycatsplus.copycats.content.copycat.fencegate.CopycatFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.fencegate.CopycatFenceGateModel;
import com.copycatsplus.copycats.content.copycat.fencegate.WrappedFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.halflayer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.halfpanel.CopycatHalfPanelBlock;
import com.copycatsplus.copycats.content.copycat.halfpanel.CopycatHalfPanelModel;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerBlock;
import com.copycatsplus.copycats.content.copycat.layer.CopycatLayerModel;
import com.copycatsplus.copycats.content.copycat.pressure_plate.*;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabModel;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock;
import com.copycatsplus.copycats.content.copycat.slice.CopycatSliceModel;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsEnhancedModel;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModel;
import com.copycatsplus.copycats.content.copycat.stairs.WrappedStairsBlock;
import com.copycatsplus.copycats.content.copycat.halflayer.CopycatHalfLayerModel;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorModel;
import com.copycatsplus.copycats.content.copycat.trapdoor.WrappedTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.verticalslice.CopycatVerticalSliceBlock;
import com.copycatsplus.copycats.content.copycat.verticalslice.CopycatVerticalSliceModel;
import com.copycatsplus.copycats.content.copycat.verticalstep.CopycatVerticalStepBlock;
import com.copycatsplus.copycats.content.copycat.verticalstep.CopycatVerticalStepModel;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallModel;
import com.copycatsplus.copycats.content.copycat.wall.WrappedWallBlock;
import com.copycatsplus.copycats.datagen.CCLootGen;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraftforge.common.Tags;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CCBlocks {
    private static final CreateRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final BlockEntry<CopycatSlabBlock> COPYCAT_SLAB =
            REGISTRATE.block("copycat_slab", CopycatSlabBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.SLABS)
                    .transform(FeatureToggle.register())
                    .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatSlabModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_SLAB.tag)
                    .transform(customItemModel("copycat_base", "slab"))
                    .register();

    public static final BlockEntry<CopycatBlockBlock> COPYCAT_BLOCK =
            REGISTRATE.block("copycat_block", CopycatBlockBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatBlockModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BLOCK.tag)
                    .transform(customItemModel("copycat_base", "block"))
                    .register();

    public static final BlockEntry<CopycatBeamBlock> COPYCAT_BEAM =
            REGISTRATE.block("copycat_beam", CopycatBeamBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatBeamModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BEAM.tag)
                    .transform(customItemModel("copycat_base", "beam"))
                    .register();

    public static final BlockEntry<CopycatHalfPanelBlock> COPYCAT_HALF_PANEL =
            REGISTRATE.block("copycat_half_panel", CopycatHalfPanelBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatHalfPanelModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "half_panel"))
                    .register();

    public static final BlockEntry<CopycatVerticalStepBlock> COPYCAT_VERTICAL_STEP =
            REGISTRATE.block("copycat_vertical_step", CopycatVerticalStepBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatVerticalStepModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_VERTICAL_STEP.tag)
                    .transform(customItemModel("copycat_base", "vertical_step"))
                    .register();

    public static final BlockEntry<CopycatStairsBlock> COPYCAT_STAIRS =
            REGISTRATE.block("copycat_stairs", CopycatStairsBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.STAIRS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> ToggleableCopycatModel.with(CopycatStairsModel::new, CopycatStairsEnhancedModel::new)))
                    .item()
                    .tag(CCTags.Items.COPYCAT_STAIRS.tag)
                    .transform(customItemModel("copycat_base", "stairs"))
                    .register();

    public static final BlockEntry<WrappedStairsBlock> WRAPPED_COPYCAT_STAIRS =
            REGISTRATE.block("wrapped_copycat_stairs", p -> new WrappedStairsBlock(Blocks.STONE::defaultBlockState, p))
                    .initialProperties(() -> Blocks.STONE_STAIRS)
                    .onRegister(b -> CopycatStairsBlock.stairs = b)
                    .tag(BlockTags.STAIRS)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_stairs", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatFenceBlock> COPYCAT_FENCE =
            REGISTRATE.block("copycat_fence", CopycatFenceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.FENCES, Tags.Blocks.FENCES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatFenceModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_FENCE.tag)
                    .transform(customItemModel("copycat_base", "fence"))
                    .register();

    public static final BlockEntry<WrappedFenceBlock> WRAPPED_COPYCAT_FENCE =
            REGISTRATE.block("wrapped_copycat_fence", WrappedFenceBlock::new)
                    .initialProperties(() -> Blocks.OAK_FENCE)
                    .onRegister(b -> CopycatFenceBlock.fence = b)
                    .tag(BlockTags.FENCES, Tags.Blocks.FENCES)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_fence", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatWallBlock> COPYCAT_WALL =
            REGISTRATE.block("copycat_wall", CopycatWallBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.WALLS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatWallModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_WALL.tag)
                    .transform(customItemModel("copycat_base", "wall"))
                    .register();

    public static final BlockEntry<WrappedWallBlock> WRAPPED_COPYCAT_WALL =
            REGISTRATE.block("wrapped_copycat_wall", WrappedWallBlock::new)
                    .initialProperties(() -> Blocks.COBBLESTONE_WALL)
                    .onRegister(b -> CopycatWallBlock.wall = b)
                    .tag(BlockTags.WALLS)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_wall", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatFenceGateBlock> COPYCAT_FENCE_GATE =
            REGISTRATE.block("copycat_fence_gate", CopycatFenceGateBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .tag(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatFenceGateModel::new))
                    .item()
                    .tag(CCTags.Items.COPYCAT_FENCE_GATE.tag)
                    .transform(customItemModel("copycat_base", "fence_gate"))
                    .register();

    public static final BlockEntry<WrappedFenceGateBlock> WRAPPED_COPYCAT_FENCE_GATE =
            REGISTRATE.block("wrapped_copycat_fence_gate", WrappedFenceGateBlock::new)
                    .initialProperties(() -> Blocks.OAK_FENCE_GATE)
                    .onRegister(b -> CopycatFenceGateBlock.fenceGate = b)
                    .tag(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, AllTags.AllBlockTags.MOVABLE_EMPTY_COLLIDER.tag)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_fence_gate", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatTrapdoorBlock> COPYCAT_TRAPDOOR =
            REGISTRATE.block("copycat_trapdoor", CopycatTrapdoorBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.TRAPDOORS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatTrapdoorModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "trapdoor"))
                    .register();

    public static final BlockEntry<WrappedTrapdoorBlock> WRAPPED_COPYCAT_TRAPDOOR =
            REGISTRATE.block("wrapped_copycat_trapdoor", WrappedTrapdoorBlock::new)
                    .initialProperties(() -> Blocks.OAK_TRAPDOOR)
                    .onRegister(b -> CopycatTrapdoorBlock.trapdoor = b)
                    .tag(BlockTags.TRAPDOORS)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_trapdoor", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatBoardBlock> COPYCAT_BOARD =
            REGISTRATE.block("copycat_board", CopycatBoardBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatBoardModel::new))
                    .loot(CCLootGen.build(CCLootGen.lootForDirections()))
                    .item()
                    .tag(CCTags.Items.COPYCAT_BOARD.tag)
                    .transform(customItemModel("copycat_base", "board"))
                    .register();

    public static final BlockEntry<CopycatByteBlock> COPYCAT_BYTE =
            REGISTRATE.block("copycat_byte", CopycatByteBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatByteModel::new))
                    .loot(CCLootGen.build(CCLootGen.lootForBytes()))
                    .item()
                    .transform(customItemModel("copycat_base", "byte"))
                    .register();

    public static final BlockEntry<CopycatLayerBlock> COPYCAT_LAYER =
            REGISTRATE.block("copycat_layer", CopycatLayerBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatLayerModel::new))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "layer"))
                    .register();

    public static final BlockEntry<CopycatHalfLayerBlock> COPYCAT_HALF_LAYER =
            REGISTRATE.block("copycat_half_layer", CopycatHalfLayerBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatHalfLayerModel::new))
                    .loot(CCLootGen.build(
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.NEGATIVE_LAYERS),
                            CCLootGen.lootForLayers(CopycatHalfLayerBlock.POSITIVE_LAYERS)
                    ))
                    .item()
                    .transform(customItemModel("copycat_base", "half_layer"))
                    .register();


    public static final BlockEntry<CopycatSliceBlock> COPYCAT_SLICE =
            REGISTRATE.block("copycat_slice", CopycatSliceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatSliceModel::new))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "slice"))
                    .register();

    public static final BlockEntry<CopycatVerticalSliceBlock> COPYCAT_VERTICAL_SLICE =
            REGISTRATE.block("copycat_vertical_slice", CopycatVerticalSliceBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatVerticalSliceModel::new))
                    .loot(CCLootGen.build(CCLootGen.lootForLayers()))
                    .item()
                    .transform(customItemModel("copycat_base", "vertical_slice"))
                    .register();


    public static final BlockEntry<CopycatWoodenButtonBlock> COPYCAT_WOODEN_BUTTON =
            REGISTRATE.block("copycat_wooden_button", CopycatWoodenButtonBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatButtonModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<CopycatStoneButtonBlock> COPYCAT_STONE_BUTTON =
            REGISTRATE.block("copycat_stone_button", CopycatStoneButtonBlock::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.STONE_BUTTONS)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatButtonModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "button"))
                    .register();

    public static final BlockEntry<WrappedButton.Wood> WRAPPED_COPYCAT_WOODEN_BUTTON =
            REGISTRATE.block("wrapped_copycat_wooden_button", p -> new WrappedButton().wood(p, BlockSetType.OAK, 30, true))
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .onRegister(b -> CopycatWoodenButtonBlock.button = b)
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.WOODEN_BUTTONS)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_wooden_button", "block/barrier")))
                    .register();

    public static final BlockEntry<WrappedButton.Stone> WRAPPED_COPYCAT_STONE_BUTTON =
            REGISTRATE.block("wrapped_copycat_stone_button", p -> new WrappedButton().stone(p, BlockSetType.STONE, 20, false))
                    .initialProperties(() -> Blocks.STONE_BUTTON)
                    .onRegister(b -> CopycatStoneButtonBlock.button = b)
                    .tag(BlockTags.BUTTONS)
                    .tag(BlockTags.STONE_BUTTONS)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_stone_button", "block/barrier")))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.Wooden> WRAPPED_COPYCAT_WOODEN_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_wooden_pressure_plate", p -> new WrappedPressurePlate().wooden(PressurePlateBlock.Sensitivity.EVERYTHING, p, BlockSetType.OAK))
                    .initialProperties(() -> Blocks.OAK_BUTTON)
                    .onRegister(b -> CopycatWoodenPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_wooden_pressure_plate", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatWoodenPressurePlate> COPYCAT_WOODEN_PRESSURE_PLATE =
            REGISTRATE.block("copycat_wooden_pressure_plate", CopycatWoodenPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.WOODEN_PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatPressurePlateModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.Stone> WRAPPED_COPYCAT_STONE_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_stone_pressure_plate", p -> new WrappedPressurePlate().stone(PressurePlateBlock.Sensitivity.MOBS, p, BlockSetType.STONE))
                    .initialProperties(() -> Blocks.STONE_BUTTON)
                    .onRegister(b -> CopycatStonePressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.STONE_PRESSURE_PLATES)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_stone_pressure_plate", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatStonePressurePlate> COPYCAT_STONE_PRESSURE_PLATE =
            REGISTRATE.block("copycat_stone_pressure_plate", CopycatStonePressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.PRESSURE_PLATES)
                    .tag(BlockTags.STONE_PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatPressurePlateModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.HeavyWeighted> WRAPPED_COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_heavy_weighted_pressure_plate", p -> new WrappedPressurePlate().heavyWeighted(150, p, BlockSetType.IRON))
                    .initialProperties(() -> Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                    .onRegister(b -> CopycatHeavyWeightedPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_heavy_weighted_pressure_plate", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatHeavyWeightedPressurePlate> COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_heavy_weighted_pressure_plate", CopycatHeavyWeightedPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatPressurePlateModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static final BlockEntry<WrappedPressurePlate.LightWeighted> WRAPPED_COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("wrapped_copycat_light_weighted_pressure_plate", p -> new WrappedPressurePlate().lightWeighted(15, p, BlockSetType.GOLD))
                    .initialProperties(() -> Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                    .onRegister(b -> CopycatLightWeightedPressurePlate.pressurePlate = b)
                    .tag(BlockTags.PRESSURE_PLATES)
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), p.models().withExistingParent("wrapped_copycat_light_weighted_pressure_plate", "block/barrier")))
                    .register();

    public static final BlockEntry<CopycatLightWeightedPressurePlate> COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE =
            REGISTRATE.block("copycat_light_weighted_pressure_plate", CopycatLightWeightedPressurePlate::new)
                    .transform(BuilderTransformers.copycat())
                    .properties(p -> p.isValidSpawn((state, level, pos, entity) -> false))
                    .tag(BlockTags.PRESSURE_PLATES)
                    .transform(FeatureToggle.register())
                    .onRegister(CreateRegistrate.blockModel(() -> CopycatPressurePlateModel::new))
                    .item()
                    .transform(customItemModel("copycat_base", "pressure_plate"))
                    .register();

    public static void register() {
    }
}
