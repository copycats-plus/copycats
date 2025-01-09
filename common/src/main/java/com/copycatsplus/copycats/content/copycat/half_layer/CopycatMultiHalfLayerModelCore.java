package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.List;
import java.util.Objects;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock.*;

public class CopycatMultiHalfLayerModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_HALF_LAYER.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, NEGATIVE_LAYERS.getName()) && state.getValue(NEGATIVE_LAYERS) == 0)
            return;
        if (Objects.equals(key, POSITIVE_LAYERS.getName()) && state.getValue(POSITIVE_LAYERS) == 0)
            return;

        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = state.getValue(AXIS) == Direction.Axis.X ? 0 : 90;
        boolean positive = key.equals(POSITIVE_LAYERS.getName());
        int layer = state.getValue(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS);
        if (layer == 0) return;
        AssemblyTransform transform = t -> t.rotateY(rot + (positive ? 180 : 0)).flipY(flipY);
        QuadAutoCull autoCull = autoCull(aabb(8, 16, 16));
        context.assemblePiece(
                transform,
                vec3(0, 0, 0),
                aabb(4, layer, 16),
                cull(EAST | UP),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(0, layer, 0),
                aabb(4, layer, 16).move(0, 16 - layer, 0),
                cull(EAST | DOWN),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(4, 0, 0),
                aabb(4, layer, 16).move(12, 0, 0),
                cull(WEST | UP),
                autoCull
        );
        context.assemblePiece(
                transform,
                vec3(4, layer, 0),
                aabb(4, layer, 16).move(12, 16 - layer, 0),
                cull(WEST | DOWN),
                autoCull
        );
    }
}
