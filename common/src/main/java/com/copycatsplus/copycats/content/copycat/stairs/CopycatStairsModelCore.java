package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatStairsModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int) state.getValue(StairBlock.FACING).toYRot();
        boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
        StairsShape shape = state.getValue(StairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                AssemblyTransform transform = t -> t.rotateY(facing).flipY(top);
                assembleStraight(context, transform, enhanced);
            }
            case INNER_LEFT, INNER_RIGHT -> {
                boolean flipX = shape == StairsShape.INNER_RIGHT;
                AssemblyTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                assembleInnerLeft(context, transform, enhanced);
            }
            case OUTER_LEFT, OUTER_RIGHT -> {
                boolean flipX = shape == StairsShape.OUTER_RIGHT;
                AssemblyTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                assembleOuterLeft(context, transform, enhanced);
            }
        }
    }

    public static void assembleStraight(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 12),
                    aabb(16, 12, 4).move(0, 4, 12),
                    cull(NORTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 8),
                    aabb(16, 8, 2).move(0, 8, 0),
                    cull(SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 7, 8),
                    aabb(16, 1, 2).move(0, 7, 0),
                    cull(NORTH | SOUTH | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 5, 10),
                    aabb(16, 11, 2).move(0, 5, 2),
                    cull(NORTH | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16).move(0, 0, 0),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 7, 0),
                    aabb(16, 1, 8).move(0, 15, 0),
                    cull(SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 5, 0),
                    aabb(16, 2, 10).move(0, 13, 0),
                    cull(SOUTH | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(16, 1, 12).move(0, 12, 0),
                    cull(SOUTH | UP | DOWN)
            );
        } else {
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 8),
                    cull(UP | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(16, 4, 8).move(0, 12, 0),
                    cull(DOWN | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 8),
                    aabb(16, 8, 8).move(0, 0, 8),
                    cull(UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 8),
                    aabb(16, 8, 4).move(0, 8, 0),
                    cull(DOWN | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 12),
                    aabb(16, 8, 4).move(0, 8, 12),
                    cull(DOWN | NORTH)
            );
        }
    }

    public static void assembleInnerLeft(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 12),
                    aabb(16, 12, 4).move(0, 4, 12),
                    cull(NORTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 4, 0),
                    aabb(4, 12, 12).move(12, 4, 0),
                    cull(SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16).move(0, 0, 0),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(12, 1, 12).move(0, 12, 0),
                    cull(EAST | SOUTH | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 5, 0),
                    aabb(10, 2, 10).move(0, 13, 0),
                    cull(EAST | SOUTH | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 7, 0),
                    aabb(8, 1, 8).move(0, 15, 0),
                    cull(EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 8),
                    aabb(8, 8, 2).move(0, 8, 0),
                    cull(EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 8),
                    aabb(1, 8, 2).move(8, 8, 0),
                    cull(NORTH | EAST | SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(9, 8, 8),
                    aabb(1, 8, 2).move(1, 8, 8),
                    cull(NORTH | EAST | SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 5, 10),
                    aabb(11, 11, 2).move(0, 5, 2),
                    cull(NORTH | EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 7, 8),
                    aabb(10, 1, 2).move(0, 7, 0),
                    cull(NORTH | EAST | SOUTH | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 7, 0),
                    aabb(2, 1, 8).move(0, 7, 0),
                    cull(EAST | SOUTH | WEST | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 0),
                    aabb(2, 8, 8).move(0, 8, 0),
                    cull(EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(10, 5, 0),
                    aabb(2, 11, 10).move(2, 5, 0),
                    cull(EAST | SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(11, 5, 10),
                    aabb(1, 11, 2).move(3, 5, 10),
                    cull(NORTH | EAST | SOUTH | WEST | DOWN)
            );
        } else {
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(8, 4, 8),
                    cull(UP | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(8, 4, 8).move(0, 12, 0),
                    cull(DOWN | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 8),
                    aabb(16, 8, 8).move(0, 0, 8),
                    cull(UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 8),
                    aabb(8, 8, 8).move(8, 8, 8),
                    cull(DOWN | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 12),
                    aabb(8, 8, 4).move(0, 8, 12),
                    cull(DOWN | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 8, 8),
                    aabb(8, 8, 4).move(0, 8, 0),
                    cull(DOWN | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 8, 0),
                    aabb(4, 8, 8).move(12, 8, 0),
                    cull(DOWN | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 0),
                    aabb(4, 8, 8).move(0, 8, 0),
                    cull(DOWN | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 0, 0),
                    aabb(8, 8, 8).move(8, 0, 0),
                    cull(UP | SOUTH | WEST)
            );
        }
    }

    public static void assembleOuterLeft(CopycatRenderContext context, AssemblyTransform transform, boolean enhanced) {
        if (enhanced) {
            context.assemblePiece(
                    transform,
                    vec3(12, 4, 12),
                    aabb(4, 12, 4).move(12, 4, 12),
                    cull(NORTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 8, 8),
                    aabb(4, 8, 1).move(12, 8, 0),
                    cull(SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 6, 9),
                    aabb(4, 10, 2).move(12, 6, 1),
                    cull(NORTH | SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 4, 11),
                    aabb(4, 12, 1).move(12, 4, 3),
                    cull(NORTH | SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 12),
                    aabb(1, 8, 4).move(0, 8, 12),
                    cull(NORTH | EAST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(9, 6, 12),
                    aabb(2, 10, 4).move(1, 6, 12),
                    cull(NORTH | EAST | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(11, 4, 12),
                    aabb(1, 12, 4).move(3, 4, 12),
                    cull(NORTH | EAST | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 8),
                    aabb(4, 8, 4).move(0, 8, 0),
                    cull(EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16).move(0, 0, 0),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(8, 4, 8).move(0, 12, 0),
                    cull(EAST | SOUTH | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 6, 0),
                    aabb(8, 2, 8).move(8, 14, 0),
                    cull(SOUTH | WEST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 4, 0),
                    aabb(8, 2, 11).move(8, 12, 0),
                    cull(SOUTH | WEST | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 6, 8),
                    aabb(8, 2, 1).move(8, 14, 8),
                    cull(NORTH | SOUTH | WEST | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 6, 8),
                    aabb(8, 2, 8).move(0, 14, 8),
                    cull(NORTH | EAST | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 8),
                    aabb(11, 2, 8).move(0, 12, 8),
                    cull(NORTH | EAST | UP | DOWN)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 6, 8),
                    aabb(1, 2, 8).move(8, 14, 8),
                    cull(NORTH | EAST | WEST | UP | DOWN)
            );
        } else {
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(8, 4, 16).move(0, 0, 0),
                    cull(UP | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(8, 4, 16).move(0, 12, 0),
                    cull(DOWN | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 0, 0),
                    aabb(8, 4, 8).move(8, 0, 0),
                    cull(UP | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 4, 0),
                    aabb(8, 4, 8).move(8, 12, 0),
                    cull(DOWN | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 0, 8),
                    aabb(8, 8, 8).move(8, 0, 8),
                    cull(UP | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 8, 12),
                    aabb(4, 8, 4).move(12, 8, 12),
                    cull(DOWN | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 12),
                    aabb(4, 8, 4).move(0, 8, 12),
                    cull(DOWN | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(12, 8, 8),
                    aabb(4, 8, 4).move(12, 8, 0),
                    cull(DOWN | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 8),
                    aabb(4, 8, 4).move(0, 8, 0),
                    cull(DOWN | SOUTH | EAST)
            );
        }
    }
}
