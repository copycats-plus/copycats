package com.copycatsplus.copycats.content.copycat.verticalslice;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.verticalslice.CopycatVerticalSliceBlock.*;

public class CopycatVerticalSliceModel extends SimpleCopycatModel {
    public CopycatVerticalSliceModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
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
