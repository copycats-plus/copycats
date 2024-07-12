package com.copycatsplus.copycats.content.copycat.beam;


import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static net.minecraft.core.Direction.Axis;

public class CopycatBeamModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatBeamBlock.AXIS);

        AssemblyTransform transform = t -> t.rotateX(axis == Axis.Y ? 90 : 0).rotateY(axis == Axis.X ? 90 : 0);

        context.assemblePiece(
                transform,
                vec3(4, 4, 0),
                aabb(4, 4, 16),
                cull(UP | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(8, 4, 0),
                aabb(4, 4, 16).move(12, 0, 0),
                cull(UP | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(4, 8, 0),
                aabb(4, 4, 16).move(0, 12, 0),
                cull(DOWN | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(8, 8, 0),
                aabb(4, 4, 16).move(12, 12, 0),
                cull(DOWN | WEST)
        );
    }
}
