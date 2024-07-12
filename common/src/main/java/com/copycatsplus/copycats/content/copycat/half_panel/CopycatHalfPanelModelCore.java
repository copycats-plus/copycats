package com.copycatsplus.copycats.content.copycat.half_panel;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock.*;

public class CopycatHalfPanelModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.UP;
            int rot = (int) offset.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 12),
                    aabb(16, 1, 4).move(0, 0, 12),
                    cull(UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 8),
                    aabb(16, 1, 4).move(0, 0, 0),
                    cull(UP | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 1, 12),
                    aabb(16, 2, 4).move(0, 14, 12),
                    cull(DOWN | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 1, 8),
                    aabb(16, 2, 4).move(0, 14, 0),
                    cull(DOWN | SOUTH)
            );
        } else if (offset.getAxis() == facing.getAxis()) {
            boolean flipY = offset.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            int rot = (int) facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 15),
                    aabb(16, 4, 1).move(0, 0, 15),
                    cull(UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 15),
                    aabb(16, 4, 1).move(0, 12, 15),
                    cull(DOWN | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 13),
                    aabb(16, 4, 2).move(0, 0, 0),
                    cull(UP | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 13),
                    aabb(16, 4, 2).move(0, 12, 0),
                    cull(DOWN | SOUTH)
            );
        } else {
            int leftOffset = offset == facing.getCounterClockWise() ? 8 : 0;
            int rot = (int) facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);
            context.assemblePiece(
                    transform,
                    vec3(leftOffset, 0, 15),
                    aabb(4, 16, 1).move(0, 0, 15),
                    cull(EAST | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4 + leftOffset, 0, 15),
                    aabb(4, 16, 1).move(12, 0, 15),
                    cull(WEST | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(leftOffset, 0, 13),
                    aabb(4, 16, 2).move(0, 0, 0),
                    cull(EAST | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4 + leftOffset, 0, 13),
                    aabb(4, 16, 2).move(12, 0, 0),
                    cull(WEST | SOUTH)
            );
        }
    }
}
