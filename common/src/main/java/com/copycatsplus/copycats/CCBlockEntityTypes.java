package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlockEntity;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorRenderer;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelInstance;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelRenderer;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeRenderer;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.ladder.MultiStateCopycatLadderBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftInstance;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftRenderer;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class CCBlockEntityTypes {
    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final BlockEntityEntry<? extends CCCopycatBlockEntity> COPYCAT =
            REGISTRATE.blockEntity("copycat", CCCopycatBlockEntity::new)
                    .validBlocks(
                            CCBlocks.COPYCAT_BLOCK,
                            CCBlocks.COPYCAT_BEAM,
                            CCBlocks.COPYCAT_VERTICAL_STEP,
                            CCBlocks.COPYCAT_HALF_PANEL,
                            CCBlocks.COPYCAT_STAIRS,
                            CCBlocks.COPYCAT_FENCE,
                            CCBlocks.COPYCAT_FENCE_GATE,
                            CCBlocks.COPYCAT_TRAPDOOR,
                            CCBlocks.COPYCAT_IRON_TRAPDOOR,
                            CCBlocks.COPYCAT_WALL,
                            CCBlocks.COPYCAT_GHOST_BLOCK,
                            CCBlocks.COPYCAT_LADDER,
                            CCBlocks.COPYCAT_LAYER,
                            CCBlocks.COPYCAT_SLICE,
                            CCBlocks.COPYCAT_VERTICAL_SLICE,
                            CCBlocks.COPYCAT_CORNER_SLICE,
                            CCBlocks.COPYCAT_WOODEN_BUTTON,
                            CCBlocks.COPYCAT_STONE_BUTTON,
                            CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_STONE_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE,
                            CCBlocks.COPYCAT_VERTICAL_STAIRS,
                            CCBlocks.COPYCAT_SLOPE,
                            CCBlocks.COPYCAT_VERTICAL_SLOPE,
                            CCBlocks.COPYCAT_SLOPE_LAYER,
                            CCBlocks.COPYCAT_DOOR,
                            CCBlocks.COPYCAT_IRON_DOOR,
                            CCBlocks.COPYCAT_PANE,
                            CCBlocks.COPYCAT_FLAT_PANE
                    )
                    .register();

    public static final BlockEntityEntry<? extends MultiStateCopycatBlockEntity> MULTI_STATE_COPYCAT =
            REGISTRATE.blockEntity("multistate_copycat", MultiStateCopycatBlockEntity::new)
                    .validBlocks(
                            CCBlocks.COPYCAT_SLAB,
                            CCBlocks.COPYCAT_BYTE,
                            CCBlocks.COPYCAT_HALF_LAYER,
                            CCBlocks.COPYCAT_VERTICAL_HALF_LAYER,
                            CCBlocks.COPYCAT_STACKED_HALF_LAYER,
                            CCBlocks.COPYCAT_BOARD,
                            CCBlocks.COPYCAT_BYTE_PANEL
                    )
                    .register();

    public static final BlockEntityEntry<? extends MultiStateCopycatBlockEntity> MULTI_STATE_COPYCAT_LADDER =
            REGISTRATE.blockEntity("multistate_ladder_copycat", MultiStateCopycatLadderBlockEntity::new)
                    .validBlocks(/*CCBlocks.COPYCAT_LADDER*/)
                    .register();

    public static final BlockEntityEntry<? extends CopycatShaftBlockEntity> COPYCAT_SHAFT =
            REGISTRATE.blockEntity("copycat_shaft", CopycatShaftBlockEntity::new)
                    .instance(() -> CopycatShaftInstance::new, false)
                    .validBlocks(CCBlocks.COPYCAT_SHAFT)
                    .renderer(() -> CopycatShaftRenderer::new)
                    .register();

    public static final BlockEntityEntry<? extends CopycatCogWheelBlockEntity> COPYCAT_COGWHEEL =
            REGISTRATE.blockEntity("copycat_cogwheel", CopycatCogWheelBlockEntity::new)
                    .instance(() -> CopycatCogWheelInstance::new, false)
                    .validBlocks(CCBlocks.COPYCAT_COGWHEEL, CCBlocks.COPYCAT_LARGE_COGWHEEL)
                    .renderer(() -> CopycatCogWheelRenderer::new)
                    .register();

    public static final BlockEntityEntry<? extends CopycatFluidPipeBlockEntity> COPYCAT_FLUID_PIPE =
            REGISTRATE.blockEntity("copycat_fluid_pipe", CopycatFluidPipeBlockEntity::new)
                    .validBlocks(CCBlocks.COPYCAT_FLUID_PIPE)
                    .renderer(() -> CopycatFluidPipeRenderer::new)
                    .register();

    public static final BlockEntityEntry<? extends CopycatStraightPipeBlockEntity> COPYCAT_GLASS_FLUID_PIPE =
            REGISTRATE.blockEntity("copycat_glass_fluid_pipe", CopycatStraightPipeBlockEntity::new)
                    .validBlocks(CCBlocks.COPYCAT_GLASS_FLUID_PIPE)
                    .renderer(() -> TransparentStraightPipeRenderer::new)
                    .register();

    public static final BlockEntityEntry<? extends CopycatSlidingDoorBlockEntity> COPYCAT_SLIDING_DOOR =
            REGISTRATE.blockEntity("copycat_sliding_door", CopycatSlidingDoorBlockEntity::new)
                    .validBlocks(CCBlocks.COPYCAT_SLIDING_DOOR, CCBlocks.COPYCAT_FOLDING_DOOR)
                    .renderer(() -> CopycatSlidingDoorRenderer::new)
                    .register();

    public static void register() {
    }
}
