package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric.CopycatFluidPipeModelFabric;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import net.minecraft.client.resources.model.BakedModel;

public class CCCustomModelsImpl {

    public static BakedModel getFluidPipeModel(BakedModel original, CopycatModelCore copycat, boolean disableAO) {
        return new CopycatFluidPipeModelFabric(original, copycat, disableAO);
    }
}
