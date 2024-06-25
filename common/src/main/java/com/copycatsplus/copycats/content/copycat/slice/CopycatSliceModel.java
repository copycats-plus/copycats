package com.copycatsplus.copycats.content.copycat.slice;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock.*;

public class CopycatSliceModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = (int) state.getValue(FACING).toYRot();
        int layers = state.getValue(LAYERS);
        GlobalTransform transform = t -> t.rotateY(rot).flipY(flipY);
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 16 - layers),
                aabb(16, layers, layers).move(0, 0, 16 - layers),
                cull(UP | NORTH)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, layers, 16 - layers),
                aabb(16, layers, layers).move(0, 16 - layers, 16 - layers),
                cull(DOWN | NORTH)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 0, 0),
                cull(UP | SOUTH)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, layers, 16 - layers * 2),
                aabb(16, layers, layers).move(0, 16 - layers, 0),
                cull(DOWN | SOUTH)
        );
    }
}
