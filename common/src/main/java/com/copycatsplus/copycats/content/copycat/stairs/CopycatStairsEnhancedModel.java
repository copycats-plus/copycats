package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatStairsEnhancedModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int facing = (int) state.getValue(StairBlock.FACING).toYRot();
        boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
        StairsShape shape = state.getValue(StairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                GlobalTransform transform = t -> t.rotateY(facing).flipY(top);
                assembleStraight(context, transform);
            }
            case INNER_LEFT, INNER_RIGHT -> {
                boolean flipX = shape == StairsShape.INNER_RIGHT;
                GlobalTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                assembleInnerLeft(context, transform);
            }
            case OUTER_LEFT, OUTER_RIGHT -> {
                boolean flipX = shape == StairsShape.OUTER_RIGHT;
                GlobalTransform transform = t -> t.flipX(flipX).rotateY(facing).flipY(top);
                assembleOuterLeft(context, transform);
            }
        }
    }

    public static void assembleStraight(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 12),
                aabb(16, 12, 4).move(0, 4, 12),
                cull(NORTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 8, 8),
                aabb(16, 8, 2).move(0, 8, 0),
                cull(SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 7, 8),
                aabb(16, 1, 2).move(0, 7, 0),
                cull(NORTH | SOUTH | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 5, 10),
                aabb(16, 11, 2).move(0, 5, 2),
                cull(NORTH | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 4, 16).move(0, 0, 0),
                cull(UP)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 7, 0),
                aabb(16, 1, 8).move(0, 15, 0),
                cull(SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 5, 0),
                aabb(16, 2, 10).move(0, 13, 0),
                cull(SOUTH | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 0),
                aabb(16, 1, 12).move(0, 12, 0),
                cull(SOUTH | UP | DOWN)
        );
    }

    public static void assembleInnerLeft(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 12),
                aabb(16, 12, 4).move(0, 4, 12),
                cull(NORTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(12, 4, 0),
                aabb(4, 12, 12).move(12, 4, 0),
                cull(SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 4, 16).move(0, 0, 0),
                cull(UP)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 0),
                aabb(12, 1, 12).move(0, 12, 0),
                cull(EAST | SOUTH | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 5, 0),
                aabb(10, 2, 10).move(0, 13, 0),
                cull(EAST | SOUTH | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 7, 0),
                aabb(8, 1, 8).move(0, 15, 0),
                cull(EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 8, 8),
                aabb(8, 8, 2).move(0, 8, 0),
                cull(EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 8, 8),
                aabb(1, 8, 2).move(8, 8, 0),
                cull(NORTH | EAST | SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(9, 8, 8),
                aabb(1, 8, 2).move(1, 8, 8),
                cull(NORTH | EAST | SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 5, 10),
                aabb(11, 11, 2).move(0, 5, 2),
                cull(NORTH | EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 7, 8),
                aabb(10, 1, 2).move(0, 7, 0),
                cull(NORTH | EAST | SOUTH | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 7, 0),
                aabb(2, 1, 8).move(0, 7, 0),
                cull(EAST | SOUTH | WEST | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 8, 0),
                aabb(2, 8, 8).move(0, 8, 0),
                cull(EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(10, 5, 0),
                aabb(2, 11, 10).move(2, 5, 0),
                cull(EAST | SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(11, 5, 10),
                aabb(1, 11, 2).move(3, 5, 10),
                cull(NORTH | EAST | SOUTH | WEST | DOWN)
        );
    }

    public static void assembleOuterLeft(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(
                context,
                transform,
                vec3(12, 4, 12),
                aabb(4, 12, 4).move(12, 4, 12),
                cull(NORTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(12, 8, 8),
                aabb(4, 8, 1).move(12, 8, 0),
                cull(SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(12, 6, 9),
                aabb(4, 10, 2).move(12, 6, 1),
                cull(NORTH | SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(12, 4, 11),
                aabb(4, 12, 1).move(12, 4, 3),
                cull(NORTH | SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 8, 12),
                aabb(1, 8, 4).move(0, 8, 12),
                cull(NORTH | EAST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(9, 6, 12),
                aabb(2, 10, 4).move(1, 6, 12),
                cull(NORTH | EAST | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(11, 4, 12),
                aabb(1, 12, 4).move(3, 4, 12),
                cull(NORTH | EAST | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 8, 8),
                aabb(4, 8, 4).move(0, 8, 0),
                cull(EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 4, 16).move(0, 0, 0),
                cull(UP)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 0),
                aabb(8, 4, 8).move(0, 12, 0),
                cull(EAST | SOUTH | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 6, 0),
                aabb(8, 2, 8).move(8, 14, 0),
                cull(SOUTH | WEST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 4, 0),
                aabb(8, 2, 11).move(8, 12, 0),
                cull(SOUTH | WEST | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 6, 8),
                aabb(8, 2, 1).move(8, 14, 8),
                cull(NORTH | SOUTH | WEST | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 6, 8),
                aabb(8, 2, 8).move(0, 14, 8),
                cull(NORTH | EAST | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(0, 4, 8),
                aabb(11, 2, 8).move(0, 12, 8),
                cull(NORTH | EAST | UP | DOWN)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 6, 8),
                aabb(1, 2, 8).move(8, 14, 8),
                cull(NORTH | EAST | WEST | UP | DOWN)
        );
    }
}
