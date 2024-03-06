package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock.*;

public class CopycatHalfLayerModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = state.getValue(AXIS) == Direction.Axis.X ? 0 : 90;
        for (boolean positive : Iterate.falseAndTrue) {
            int layer = state.getValue(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS);
            if (layer == 0) continue;
            assemblePiece(
                    context, rot + (positive ? 180 : 0), flipY,
                    vec3(0, 0, 0),
                    aabb(4, layer, 16),
                    cull(EAST | UP)
            );
            assemblePiece(
                    context, rot + (positive ? 180 : 0), flipY,
                    vec3(0, layer, 0),
                    aabb(4, layer, 16).move(0, 16 - layer, 0),
                    cull(EAST | DOWN)
            );
            assemblePiece(
                    context, rot + (positive ? 180 : 0), flipY,
                    vec3(4, 0, 0),
                    aabb(4, layer, 16).move(12, 0, 0),
                    cull(WEST | UP)
            );
            assemblePiece(
                    context, rot + (positive ? 180 : 0), flipY,
                    vec3(4, layer, 0),
                    aabb(4, layer, 16).move(12, 16 - layer, 0),
                    cull(WEST | DOWN)
            );
        }
    }
}
