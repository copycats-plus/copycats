package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.content.copycat.base.CopycatBaseBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.world.level.block.Block;

public class CCBlockStateGen {

    @ExpectPlatform
    public static void glassPipe(DataGenContext<Block, CopycatGlassFluidPipeBlock> c, RegistrateBlockstateProvider p) {
    }

    @ExpectPlatform
    public static void base(DataGenContext<Block, CopycatBaseBlock> c, RegistrateBlockstateProvider p) {
    }
}
