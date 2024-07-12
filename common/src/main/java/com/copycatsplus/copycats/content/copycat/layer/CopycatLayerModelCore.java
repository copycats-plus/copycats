package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatLayerModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int layer = state.getValue(CopycatLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatLayerBlock.FACING);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.DOWN;
            AssemblyTransform transform = t -> t.flipY(flipY);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, layer, 16),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, layer, 0),
                    aabb(16, layer, 16).move(0, 16 - layer, 0),
                    cull(DOWN)
            );
        } else {
            int rot = (int) facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, layer),
                    cull(SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, layer),
                    aabb(16, 16, layer).move(0, 0, 16 - layer),
                    cull(NORTH)
            );
        }
    }
}
