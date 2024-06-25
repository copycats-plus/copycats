package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatStairsModel implements SimpleCopycatPart {

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
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 4, 8),
                cull(UP | SOUTH)
        );
        assemblePiece(context,
                transform,
                vec3(0, 4, 0),
                aabb(16, 4, 8).move(0, 12, 0),
                cull(DOWN | SOUTH)
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 8),
                aabb(16, 8, 8).move(0, 0, 8),
                cull(UP | NORTH)
        );
        assemblePiece(context,
                transform,
                vec3(0, 8, 8),
                aabb(16, 8, 4).move(0, 8, 0),
                cull(DOWN | SOUTH)
        );
        assemblePiece(context,
                transform,
                vec3(0, 8, 12),
                aabb(16, 8, 4).move(0, 8, 12),
                cull(DOWN | NORTH)
        );
    }

    public static void assembleInnerLeft(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(8, 4, 8),
                cull(UP | SOUTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(0, 4, 0),
                aabb(8, 4, 8).move(0, 12, 0),
                cull(DOWN | SOUTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 8),
                aabb(16, 8, 8).move(0, 0, 8),
                cull(UP | NORTH)
        );
        assemblePiece(context,
                transform,
                vec3(8, 8, 8),
                aabb(8, 8, 8).move(8, 8, 8),
                cull(DOWN | NORTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(0, 8, 12),
                aabb(8, 8, 4).move(0, 8, 12),
                cull(DOWN | NORTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(0, 8, 8),
                aabb(8, 8, 4).move(0, 8, 0),
                cull(DOWN | SOUTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(12, 8, 0),
                aabb(4, 8, 8).move(12, 8, 0),
                cull(DOWN | SOUTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 8, 0),
                aabb(4, 8, 8).move(0, 8, 0),
                cull(DOWN | SOUTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 0, 0),
                aabb(8, 8, 8).move(8, 0, 0),
                cull(UP | SOUTH | WEST)
        );
    }

    public static void assembleOuterLeft(CopycatRenderContext<?, ?> context, GlobalTransform transform) {
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(8, 4, 16).move(0, 0, 0),
                cull(UP | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(0, 4, 0),
                aabb(8, 4, 16).move(0, 12, 0),
                cull(DOWN | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 0, 0),
                aabb(8, 4, 8).move(8, 0, 0),
                cull(UP | SOUTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 4, 0),
                aabb(8, 4, 8).move(8, 12, 0),
                cull(DOWN | SOUTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 0, 8),
                aabb(8, 8, 8).move(8, 0, 8),
                cull(UP | NORTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(12, 8, 12),
                aabb(4, 8, 4).move(12, 8, 12),
                cull(DOWN | NORTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 8, 12),
                aabb(4, 8, 4).move(0, 8, 12),
                cull(DOWN | NORTH | EAST)
        );
        assemblePiece(context,
                transform,
                vec3(12, 8, 8),
                aabb(4, 8, 4).move(12, 8, 0),
                cull(DOWN | SOUTH | WEST)
        );
        assemblePiece(context,
                transform,
                vec3(8, 8, 8),
                aabb(4, 8, 4).move(0, 8, 0),
                cull(DOWN | SOUTH | EAST)
        );
    }
}
