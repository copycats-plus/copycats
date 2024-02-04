package com.copycatsplus.copycats.content.copycat.fence;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatFenceModel extends SimpleCopycatModel {

    public CopycatFenceModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        for (Direction direction : Iterate.horizontalDirections) {
            assemblePiece(context, (int) direction.toYRot(), false,
                    vec3(6, 0, 6),
                    aabb(2, 16, 2),
                    cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
            );
        }

        for (Direction direction : Iterate.horizontalDirections) {
            if (!state.getValue(CopycatFenceBlock.byDirection(direction))) continue;

            int rot = (int) direction.toYRot();
            assemblePiece(context, rot, false,
                    vec3(7, 6, 10),
                    aabb(1, 1, 6),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(context, rot, false,
                    vec3(8, 6, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
            assemblePiece(context, rot, false,
                    vec3(7, 7, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(context, rot, false,
                    vec3(8, 7, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
            );

            assemblePiece(context, rot, false,
                    vec3(7, 12, 10),
                    aabb(1, 1, 6),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(context, rot, false,
                    vec3(8, 12, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
            assemblePiece(context, rot, false,
                    vec3(7, 13, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(context, rot, false,
                    vec3(8, 13, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
        }
    }
}
