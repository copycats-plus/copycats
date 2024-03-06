package com.copycatsplus.copycats.content.copycat.vertical_slice;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.vertical_slice.CopycatVerticalSliceBlock.*;

public class CopycatVerticalSliceModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);

        assemblePiece(
                context, rot, false,
                vec3(16 - layers, 0, 16 - layers),
                aabb(layers, 16, layers).move(16 - layers, 0, 16 - layers),
                cull(WEST | NORTH)
        );
        assemblePiece(
                context, rot, false,
                vec3(16 - layers, 0, 16 - layers * 2),
                aabb(layers, 16, layers).move(16 - layers, 0, 0),
                cull(WEST | SOUTH)
        );
        assemblePiece(
                context, rot, false,
                vec3(16 - layers * 2, 0, 16 - layers),
                aabb(layers, 16, layers).move(0, 0, 16 - layers),
                cull(EAST | NORTH)
        );
        assemblePiece(
                context, rot, false,
                vec3(16 - layers * 2, 0, 16 - layers * 2),
                aabb(layers, 16, layers).move(0, 0, 0),
                cull(EAST | SOUTH)
        );
    }
}
