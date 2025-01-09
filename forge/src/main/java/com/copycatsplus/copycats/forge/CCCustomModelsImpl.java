package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.forge.CopycatFluidPipeModelForge;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import net.minecraft.client.resources.model.BakedModel;

public class CCCustomModelsImpl {

    public static BakedModel getFluidPipeModel(BakedModel original, CopycatModelCore copycat, boolean disableAO) {
        return new CopycatFluidPipeModelForge(original, copycat, disableAO);
    }
}
