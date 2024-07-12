package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric.CopycatFluidPipeModelFabric;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;

public class CCBlocksImpl {

    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        p.simpleBlock(c.getEntry(), p.models().withExistingParent(name, "block/barrier"));
    }

    public static BakedModel getFluidPipeModel(BakedModel original, CopycatModelCore copycat) {
        return new CopycatFluidPipeModelFabric(original, copycat);
    }
}
