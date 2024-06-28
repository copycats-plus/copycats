package com.copycatsplus.copycats.content.copycat.slope_layer;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class CopycatSlopeLayerModel implements SimpleCopycatPart {

    private final boolean enhanced;

    public CopycatSlopeLayerModel(boolean enhanced) {
        this.enhanced = enhanced;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int layer = state.getValue(CopycatSlopeLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatSlopeLayerBlock.FACING);
        Half half = state.getValue(CopycatSlopeLayerBlock.HALF);

        GlobalTransform transform = t -> t.rotateY((int) facing.toYRot()).flipY(half == Half.TOP);

        if (layer <= 4)
            CopycatSlopeModel.assembleSlope(context, transform, 0, layer * 4, enhanced);
        else
            CopycatSlopeModel.assembleSlope(context, transform, (layer - 4) * 4, 16, enhanced);
    }
}
