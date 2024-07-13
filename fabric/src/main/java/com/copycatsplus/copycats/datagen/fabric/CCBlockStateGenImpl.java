package com.copycatsplus.copycats.datagen.fabric;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CCBlockStateGenImpl {

    public static void glassPipe(DataGenContext<Block, CopycatGlassFluidPipeBlock> c, RegistrateBlockstateProvider p) {
        p.getVariantBuilder(c.getEntry())
                .forAllStatesExcept(state -> {
                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                    return ConfiguredModel.builder()
                            .modelFile(p.models()
                                    .getExistingFile(p.modLoc("block/fluid_pipe/window")))
                            .uvLock(false)
                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                            .build();
                }, BlockStateProperties.WATERLOGGED);
    }

    public static void base(DataGenContext<Block, CopycatBaseBlock> c, RegistrateBlockstateProvider p) {
        p.getVariantBuilder(c.getEntry())
                .forAllStatesExcept(state -> {
                    int baseType = state.getValue(CCBlockStateProperties.BASE_TYPE);
                    return ConfiguredModel.builder()
                            .modelFile(p.models().cubeAll(c.getName() + "_" + baseType, p.modLoc("block/copycat_base_" + baseType)))
                            .build();
                });
    }
}
