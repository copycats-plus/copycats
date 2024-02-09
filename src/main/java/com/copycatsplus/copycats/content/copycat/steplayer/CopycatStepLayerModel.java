package com.copycatsplus.copycats.content.copycat.steplayer;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.steplayer.CopycatStepLayerBlock.*;
import static net.minecraft.core.Direction.Axis;

public class CopycatStepLayerModel extends SimpleCopycatModel {
    public CopycatStepLayerModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        boolean flipY = state.getValue(HALF) == Half.TOP;
        int rot = state.getValue(AXIS) == Axis.X ? 0 : 90;
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
