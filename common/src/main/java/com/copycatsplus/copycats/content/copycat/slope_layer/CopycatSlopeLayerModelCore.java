package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModelCore;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class CopycatSlopeLayerModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int layer = state.getValue(CopycatSlopeLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatSlopeLayerBlock.FACING);
        Half half = state.getValue(CopycatSlopeLayerBlock.HALF);

        AssemblyTransform transform = t -> t.rotateY((int) facing.toYRot()).flipY(half == Half.TOP);

        if (layer <= 4)
            CopycatSlopeModelCore.assembleSlope(context, transform, 0, layer * 4, enhanced);
        else
            CopycatSlopeModelCore.assembleSlope(context, transform, (layer - 4) * 4, 16, enhanced);
    }
}
