package com.copycatsplus.copycats.content.copycat.slice;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock.*;

public class CopycatSliceModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);
        assemblePiece(
                context, rot, flipY,
                vec3(0, 0, 16 - layers),
                aabb(16, layers, layers).move(0, 0, 16 - layers),
                cull(UP | NORTH)
        );
        assemblePiece(
                context, rot, flipY,
                vec3(0, layers, 16 - layers),
                aabb(16, layers, layers).move(0, 16 - layers, 16 - layers),
                cull(DOWN | NORTH)
        );
        assemblePiece(
                context, rot, flipY,
                vec3(0, 0, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 0, 0),
                cull(UP | SOUTH)
        );
        assemblePiece(
                context, rot, flipY,
                vec3(0, layers, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 16 - layers, 0),
                cull(DOWN | SOUTH)
        );
    }
}
