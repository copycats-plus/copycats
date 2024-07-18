package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric.CopycatFluidPipeModelFabric;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;

public class CopycatModelCoreImpl {

    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelFabric(original, core, false);
    }

    @NotNull
    public static BakedModel createKineticModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelFabric(original, core, true);
    }

    @Environment(EnvType.CLIENT)
    @NotNull
    public static BakedModel createFluidPipeModel(BakedModel original, CopycatModelCore copycat) {
        return new CopycatFluidPipeModelFabric(original, copycat);
    }
}
