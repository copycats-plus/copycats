package com.copycatsplus.copycats.content.copycat.halfpanel;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.halfpanel.CopycatHalfPanelBlock.*;

public class CopycatHalfPanelModel extends SimpleCopycatModel {

    public CopycatHalfPanelModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getValue(FACING);
        Direction offset = state.getValue(OFFSET);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.UP;
            int rot = (int) offset.toYRot();

            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 12),
                    aabb(16, 1, 4).move(0, 0, 12),
                    cull(UP | NORTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 8),
                    aabb(16, 1, 4).move(0, 0, 0),
                    cull(UP | SOUTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 1, 12),
                    aabb(16, 2, 4).move(0, 14, 12),
                    cull(DOWN | NORTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 1, 8),
                    aabb(16, 2, 4).move(0, 14, 0),
                    cull(DOWN | SOUTH)
            );
        } else if (offset.getAxis() == facing.getAxis()) {
            boolean flipY = offset.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            int rot = (int) facing.toYRot();

            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 15),
                    aabb(16, 4, 1).move(0, 0, 15),
                    cull(UP | NORTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 4, 15),
                    aabb(16, 4, 1).move(0, 12, 15),
                    cull(DOWN | NORTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 13),
                    aabb(16, 4, 2).move(0, 0, 0),
                    cull(UP | SOUTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 4, 13),
                    aabb(16, 4, 2).move(0, 12, 0),
                    cull(DOWN | SOUTH)
            );
        } else {
            int leftOffset = offset == facing.getCounterClockWise() ? 8 : 0;
            int rot = (int) facing.toYRot();

            assemblePiece(
                    context, rot, false,
                    vec3(leftOffset, 0, 15),
                    aabb(4, 16, 1).move(0, 0, 15),
                    cull(EAST | NORTH)
            );
            assemblePiece(
                    context, rot, false,
                    vec3(4 + leftOffset, 0, 15),
                    aabb(4, 16, 1).move(12, 0, 15),
                    cull(WEST | NORTH)
            );
            assemblePiece(
                    context, rot, false,
                    vec3(leftOffset, 0, 13),
                    aabb(4, 16, 2).move(0, 0, 0),
                    cull(EAST | SOUTH)
            );
            assemblePiece(
                    context, rot, false,
                    vec3(4 + leftOffset, 0, 13),
                    aabb(4, 16, 2).move(12, 0, 0),
                    cull(WEST | SOUTH)
            );
        }
    }
}
