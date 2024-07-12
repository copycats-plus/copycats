package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlockStateProperties.VerticalStairShape;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsModelCore;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatVerticalStairsModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        VerticalStairShape shape = state.getValue(CopycatVerticalStairBlock.SHAPE);
        CCBlockStateProperties.Side side = state.getValue(CopycatVerticalStairBlock.SIDE);

        switch (shape) {
            case STRAIGHT -> {
                boolean flipX = side.isRight();
                AssemblyTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).rotateY(facing);
                CopycatStairsModelCore.assembleStraight(context, transform, enhanced);
            }
            case INNER_BOTTOM, INNER_TOP -> {
                boolean flipY = shape.isTop();
                boolean flipX = side.isRight();
                AssemblyTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).flipY(flipY).rotateY(facing);
                CopycatStairsModelCore.assembleInnerLeft(context, transform, enhanced);
            }
            case OUTER_BOTTOM, OUTER_TOP -> {
                boolean flipY = shape.isTop();
                boolean flipX = side.isRight();
                AssemblyTransform transform = t -> t.rotateX(90).rotateZ(90).flipX(flipX).flipY(flipY).rotateY(facing);
                CopycatStairsModelCore.assembleOuterLeft(context, transform, enhanced);
            }
        }
    }
}
