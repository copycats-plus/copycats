package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftInstance;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftRenderer;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CCBlockEntityTypes {
    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final BlockEntityEntry<CopycatBlockEntity> COPYCAT =
            REGISTRATE.blockEntity("copycat", CopycatBlockEntity::new)
                    .validBlocks(
                            CCBlocks.COPYCAT_BLOCK,
                            CCBlocks.COPYCAT_BEAM,
                            CCBlocks.COPYCAT_VERTICAL_STEP,
                            CCBlocks.COPYCAT_HALF_PANEL,
                            CCBlocks.COPYCAT_STAIRS,
                            CCBlocks.COPYCAT_FENCE,
                            CCBlocks.COPYCAT_FENCE_GATE,
                            CCBlocks.COPYCAT_TRAPDOOR,
                            CCBlocks.COPYCAT_WALL,
                            CCBlocks.COPYCAT_GHOST_BLOCK,
                            CCBlocks.COPYCAT_LADDER,
                            CCBlocks.COPYCAT_LAYER,
                            CCBlocks.COPYCAT_SLICE,
                            CCBlocks.COPYCAT_VERTICAL_SLICE,
                            CCBlocks.COPYCAT_WOODEN_BUTTON,
                            CCBlocks.COPYCAT_STONE_BUTTON,
                            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_VERTICAL_STAIRS,
                            CCBlocks.COPYCAT_SLOPE,
                            CCBlocks.COPYCAT_VERTICAL_SLOPE,
                            CCBlocks.COPYCAT_SLOPE_LAYER
                    )
                    .register();

    public static final BlockEntityEntry<? extends MultiStateCopycatBlockEntity> MULTI_STATE_COPYCAT_BLOCK_ENTITY =
            REGISTRATE.blockEntity("multistate_copycat", getPlatformMultiState())
                    .validBlocks(
                            CCBlocks.COPYCAT_SLAB,
                            CCBlocks.COPYCAT_BYTE,
                            CCBlocks.COPYCAT_HALF_LAYER,
                            CCBlocks.COPYCAT_BOARD
                    )
                    .register();

    public static final BlockEntityEntry<? extends MultiStateCopycatBlockEntity> MULTI_STATE_COPYCAT_LADDER_BLOCK_ENTITY =
            REGISTRATE.blockEntity("multistate_ladder_copycat", getPlatformMultiStateLadder())
                    .validBlocks(/*CCBlocks.COPYCAT_LADDER*/)
                    .register();

    public static final BlockEntityEntry<? extends CopycatShaftBlockEntity> COPYCAT_SHAFT =
            REGISTRATE.blockEntity("copycat_shaft", getPlatformShaft())
                    .instance(() -> CopycatShaftInstance::new, false)
                    .validBlocks(CCBlocks.COPYCAT_SHAFT)
                    .renderer(() -> CopycatShaftRenderer::new)
                    .register();

    @ExpectPlatform
    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        throw new AssertionError("This shouldn't appear");
    }

    @ExpectPlatform
    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatLadderMultiStateBlockEntity> getPlatformMultiStateLadder() {
        throw new AssertionError("This shouldn't appear");
    }

    @ExpectPlatform
    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatShaftBlockEntity> getPlatformShaft() {
        throw new AssertionError("This shouldn't appear");
    }

    public static void register() {
    }
}
