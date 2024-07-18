package com.copycatsplus.copycats.foundation.copycat.model.forge;

import com.copycatsplus.copycats.content.copycat.fluid_pipe.forge.CopycatFluidPipeModelForge;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CopycatModelCoreImpl {

    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelForge(original, core, false);
    }

    @NotNull
    public static BakedModel createKineticModel(BakedModel original, CopycatModelCore core) {
        return new CopycatModelForge(original, core, true);
    }

    @OnlyIn(Dist.CLIENT)
    @NotNull
    public static BakedModel createFluidPipeModel(BakedModel original, CopycatModelCore copycat) {
        return new CopycatFluidPipeModelForge(original, copycat);
    }
}
