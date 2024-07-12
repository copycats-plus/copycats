package com.copycatsplus.copycats.content.copycat.vertical_step;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatVerticalStepModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getValue(CopycatVerticalStepBlock.FACING);

        AssemblyTransform transform = t -> t.rotateY((int) facing.toYRot());

        context.assemblePiece(
                transform,
                vec3(8, 0, 8),
                aabb(4, 16, 4),
                cull(EAST | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(12, 0, 8),
                aabb(4, 16, 4).move(12, 0, 0),
                cull(WEST | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(8, 0, 12),
                aabb(4, 16, 4).move(0, 0, 12),
                cull(EAST | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(12, 0, 12),
                aabb(4, 16, 4).move(12, 0, 12),
                cull(WEST | NORTH)
        );
    }
}
