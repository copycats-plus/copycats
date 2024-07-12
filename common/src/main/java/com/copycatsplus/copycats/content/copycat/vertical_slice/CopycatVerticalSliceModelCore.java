package com.copycatsplus.copycats.content.copycat.vertical_slice;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock.*;

public class CopycatVerticalSliceModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);
        AssemblyTransform transform = t -> t.rotateY(rot);
        context.assemblePiece(
                transform,
                vec3(16 - layers, 0, 16 - layers),
                aabb(layers, 16, layers).move(16 - layers, 0, 16 - layers),
                cull(WEST | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers, 0, 16 - layers * 2),
                aabb(layers, 16, layers).move(16 - layers, 0, 0),
                cull(WEST | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, 0, 16 - layers),
                aabb(layers, 16, layers).move(0, 0, 16 - layers),
                cull(EAST | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, 0, 16 - layers * 2),
                aabb(layers, 16, layers).move(0, 0, 0),
                cull(EAST | SOUTH)
        );
    }
}
