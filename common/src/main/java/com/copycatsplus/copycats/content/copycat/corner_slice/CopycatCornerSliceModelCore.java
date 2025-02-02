package com.copycatsplus.copycats.content.copycat.corner_slice;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatCornerSliceModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);
        AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
        context.assemblePiece(
                transform,
                vec3(16 - layers, 0, 16 - layers),
                aabb(layers, layers, layers).move(16 - layers, 0, 16 - layers),
                cull(UP | NORTH | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, 0, 16 - layers),
                aabb(layers, layers, layers).move(0, 0, 16 - layers),
                cull(UP | NORTH | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers, 0, 16 - layers * 2),
                aabb(layers, layers, layers).move(16 - layers, 0, 0),
                cull(UP | SOUTH | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, 0, 16 - layers * 2),
                aabb(layers, layers, layers).move(0, 0, 0),
                cull(UP | SOUTH | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers, layers, 16 - layers),
                aabb(layers, layers, layers).move(16 - layers, 16 - layers, 16 - layers),
                cull(DOWN | NORTH | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, layers, 16 - layers),
                aabb(layers, layers, layers).move(0, 16 - layers, 16 - layers),
                cull(DOWN | NORTH | EAST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers, layers, 16 - layers * 2),
                aabb(layers, layers, layers).move(16 - layers, 16 - layers, 0),
                cull(DOWN | SOUTH | WEST)
        );
        context.assemblePiece(
                transform,
                vec3(16 - layers * 2, layers, 16 - layers * 2),
                aabb(layers, layers, layers).move(0, 16 - layers, 0),
                cull(DOWN | SOUTH | EAST)
        );
    }
}
