package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock.*;

public class CopycatMultiHalfLayerModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        if (Objects.equals(key, NEGATIVE_LAYERS.getName()) && state.getValue(NEGATIVE_LAYERS) == 0)
            return;
        if (Objects.equals(key, POSITIVE_LAYERS.getName()) && state.getValue(POSITIVE_LAYERS) == 0)
            return;

        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = state.getValue(AXIS) == Direction.Axis.X ? 0 : 90;
        boolean positive = key.equals(POSITIVE_LAYERS.getName());
        int layer = state.getValue(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS);
        if (layer == 0) return;
        GlobalTransform transform = t -> t.rotateY(rot + (positive ? 180 : 0)).flipY(flipY);
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 0),
                aabb(4, layer, 16),
                cull(EAST | UP)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, layer, 0),
                aabb(4, layer, 16).move(0, 16 - layer, 0),
                cull(EAST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(4, 0, 0),
                aabb(4, layer, 16).move(12, 0, 0),
                cull(WEST | UP)
        );
        assemblePiece(
                context,
                transform,
                vec3(4, layer, 0),
                aabb(4, layer, 16).move(12, 16 - layer, 0),
                cull(WEST | DOWN)
        );
    }
}
