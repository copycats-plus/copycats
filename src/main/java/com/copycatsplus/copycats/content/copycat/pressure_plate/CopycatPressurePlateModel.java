package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatPressurePlateModel extends SimpleCopycatModel {

    public CopycatPressurePlateModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        assemblePiece(
                context, 0, false,
                vec3(1, 0, 1),
                aabb(7, 1, 7).move(0, 0, 0),
                cull(SOUTH | EAST)
        );
        assemblePiece(
                context, 0, false,
                vec3(8, 0, 8),
                aabb(7, 1, 7).move(9, 0, 9),
                cull(NORTH | WEST)
        );
        assemblePiece(
                context, 0, false,
                vec3(8, 0, 1),
                aabb(7, 1, 7).move(9, 0, 0),
                cull(WEST | SOUTH)
        );
        assemblePiece(
                context, 0, false,
                vec3(1, 0, 8),
                aabb(7, 1, 7).move(0, 0, 9),
                cull(NORTH | EAST)
        );
    }
}
