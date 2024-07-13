package com.copycatsplus.copycats.content.copycat.slice;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock.*;

public class CopycatSliceModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);
        AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
        context.assemblePiece(
                transform,
                vec3(0, 0, 16 - layers),
                aabb(16, layers, layers).move(0, 0, 16 - layers),
                cull(UP | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(0, layers, 16 - layers),
                aabb(16, layers, layers).move(0, 16 - layers, 16 - layers),
                cull(DOWN | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(0, 0, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 0, 0),
                cull(UP | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(0, layers, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 16 - layers, 0),
                cull(DOWN | SOUTH)
        );
    }
}
