package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatLayerModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int layer = state.getValue(CopycatLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatLayerBlock.FACING);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.DOWN;
            assemblePiece(
                    context, 0, flipY,
                    vec3(0, 0, 0),
                    aabb(16, layer, 16),
                    cull(UP)
            );
            assemblePiece(
                    context, 0, flipY,
                    vec3(0, layer, 0),
                    aabb(16, layer, 16).move(0, 16 - layer, 0),
                    cull(DOWN)
            );
        } else {
            int rot = (int) facing.toYRot();
            assemblePiece(
                    context, rot, false,
                    vec3(0, 0, 0),
                    aabb(16, 16, layer),
                    cull(SOUTH)
            );
            assemblePiece(
                    context, rot, false,
                    vec3(0, 0, layer),
                    aabb(16, 16, layer).move(0, 0, 16 - layer),
                    cull(NORTH)
            );
        }
    }
}
