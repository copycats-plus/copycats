package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatLayerModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int layer = state.getValue(CopycatLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatLayerBlock.FACING);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.DOWN;
            GlobalTransform transform = t -> t.flipY(flipY);
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, layer, 16),
                    cull(UP)
            );
            assemblePiece(
                    context,
                    transform,
                    vec3(0, layer, 0),
                    aabb(16, layer, 16).move(0, 16 - layer, 0),
                    cull(DOWN)
            );
        } else {
            int rot = (int) facing.toYRot();
            GlobalTransform transform = t -> t.rotateY(rot);
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, layer),
                    cull(SOUTH)
            );
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, layer),
                    aabb(16, 16, layer).move(0, 0, 16 - layer),
                    cull(NORTH)
            );
        }
    }
}
