package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatStairsEnhancedModel extends SimpleCopycatModel {

    public CopycatStairsEnhancedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int) state.getValue(StairBlock.FACING).toYRot();
        boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
        StairsShape shape = state.getValue(StairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 12),
                        aabb(16, 12, 4).move(0, 4, 12),
                        cull(NORTH | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 8, 8),
                        aabb(16, 8, 2).move(0, 8, 0),
                        cull(SOUTH | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 7, 8),
                        aabb(16, 1, 2).move(0, 7, 0),
                        cull(NORTH | SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 5, 10),
                        aabb(16, 11, 2).move(0, 5, 2),
                        cull(NORTH | SOUTH | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 0, 0),
                        aabb(16, 4, 16).move(0, 0, 0),
                        cull(UP)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 7, 0),
                        aabb(16, 1, 8).move(0, 15, 0),
                        cull(SOUTH | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 5, 0),
                        aabb(16, 2, 10).move(0, 13, 0),
                        cull(SOUTH | UP | DOWN)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 0),
                        aabb(16, 1, 12).move(0, 12, 0),
                        cull(SOUTH | UP | DOWN)
                );
            }
            case INNER_LEFT, INNER_RIGHT -> {
                boolean flipX = shape == StairsShape.INNER_RIGHT;
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 12).flipX(flipX),
                        aabb(16, 12, 4).move(0, 4, 12).flipX(flipX),
                        cull(NORTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(12, 4, 0).flipX(flipX),
                        aabb(4, 12, 12).move(12, 4, 0).flipX(flipX),
                        cull(SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 0, 0).flipX(flipX),
                        aabb(16, 4, 16).move(0, 0, 0).flipX(flipX),
                        cull(UP).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 0).flipX(flipX),
                        aabb(12, 1, 12).move(0, 12, 0).flipX(flipX),
                        cull(EAST | SOUTH | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 5, 0).flipX(flipX),
                        aabb(10, 2, 10).move(0, 13, 0).flipX(flipX),
                        cull(EAST | SOUTH | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 7, 0).flipX(flipX),
                        aabb(8, 1, 8).move(0, 15, 0).flipX(flipX),
                        cull(EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 8, 8).flipX(flipX),
                        aabb(8, 8, 2).move(0, 8, 0).flipX(flipX),
                        cull(EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 8, 8).flipX(flipX),
                        aabb(1, 8, 2).move(8, 8, 0).flipX(flipX),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(9, 8, 8).flipX(flipX),
                        aabb(1, 8, 2).move(1, 8, 8).flipX(flipX),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 5, 10).flipX(flipX),
                        aabb(11, 11, 2).move(0, 5, 2).flipX(flipX),
                        cull(NORTH | EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 7, 8).flipX(flipX),
                        aabb(10, 1, 2).move(0, 7, 0).flipX(flipX),
                        cull(NORTH | EAST | SOUTH | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 7, 0).flipX(flipX),
                        aabb(2, 1, 8).move(0, 7, 0).flipX(flipX),
                        cull(EAST | SOUTH | WEST | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 8, 0).flipX(flipX),
                        aabb(2, 8, 8).move(0, 8, 0).flipX(flipX),
                        cull(EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(10, 5, 0).flipX(flipX),
                        aabb(2, 11, 10).move(2, 5, 0).flipX(flipX),
                        cull(EAST | SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(11, 5, 10).flipX(flipX),
                        aabb(1, 11, 2).move(3, 5, 10).flipX(flipX),
                        cull(NORTH | EAST | SOUTH | WEST | DOWN).flipX(flipX)
                );
            }
            case OUTER_LEFT, OUTER_RIGHT -> {
                boolean flipX = shape == StairsShape.OUTER_RIGHT;
                assemblePiece(
                        context, facing, top,
                        vec3(12, 4, 12).flipX(flipX),
                        aabb(4, 12, 4).move(12, 4, 12).flipX(flipX),
                        cull(NORTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(12, 8, 8).flipX(flipX),
                        aabb(4, 8, 1).move(12, 8, 0).flipX(flipX),
                        cull(SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(12, 6, 9).flipX(flipX),
                        aabb(4, 10, 2).move(12, 6, 1).flipX(flipX),
                        cull(NORTH | SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(12, 4, 11).flipX(flipX),
                        aabb(4, 12, 1).move(12, 4, 3).flipX(flipX),
                        cull(NORTH | SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 8, 12).flipX(flipX),
                        aabb(1, 8, 4).move(0, 8, 12).flipX(flipX),
                        cull(NORTH | EAST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(9, 6, 12).flipX(flipX),
                        aabb(2, 10, 4).move(1, 6, 12).flipX(flipX),
                        cull(NORTH | EAST | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(11, 4, 12).flipX(flipX),
                        aabb(1, 12, 4).move(3, 4, 12).flipX(flipX),
                        cull(NORTH | EAST | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 8, 8).flipX(flipX),
                        aabb(4, 8, 4).move(0, 8, 0).flipX(flipX),
                        cull(EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 0, 0).flipX(flipX),
                        aabb(16, 4, 16).move(0, 0, 0).flipX(flipX),
                        cull(UP).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 0).flipX(flipX),
                        aabb(8, 4, 8).move(0, 12, 0).flipX(flipX),
                        cull(EAST | SOUTH | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 6, 0).flipX(flipX),
                        aabb(8, 2, 8).move(8, 14, 0).flipX(flipX),
                        cull(SOUTH | WEST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 4, 0).flipX(flipX),
                        aabb(8, 2, 11).move(8, 12, 0).flipX(flipX),
                        cull(SOUTH | WEST | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 6, 8).flipX(flipX),
                        aabb(8, 2, 1).move(8, 14, 8).flipX(flipX),
                        cull(NORTH | SOUTH | WEST | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 6, 8).flipX(flipX),
                        aabb(8, 2, 8).move(0, 14, 8).flipX(flipX),
                        cull(NORTH | EAST | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(0, 4, 8).flipX(flipX),
                        aabb(11, 2, 8).move(0, 12, 8).flipX(flipX),
                        cull(NORTH | EAST | UP | DOWN).flipX(flipX)
                );
                assemblePiece(
                        context, facing, top,
                        vec3(8, 6, 8).flipX(flipX),
                        aabb(1, 2, 8).move(8, 14, 8).flipX(flipX),
                        cull(NORTH | EAST | WEST | UP | DOWN).flipX(flipX)
                );
            }
        }
    }
}
