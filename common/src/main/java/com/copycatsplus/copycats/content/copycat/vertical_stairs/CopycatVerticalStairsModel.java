package com.copycatsplus.copycats.content.copycat.vertical_stairs;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StairsShape;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatVerticalStairsModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int facing = (int) state.getValue(CopycatVerticalStairBlock.FACING).toYRot();
        StairsShape shape = state.getValue(CopycatVerticalStairBlock.STAIRS_SHAPE);
        switch (shape) {
            case STRAIGHT -> {
                assemblePiece(context, facing, false,
                        vec3(8, 0, 0),
                        aabb(8, 16, 4).move(8, 0, 0),
                        cull(SOUTH | WEST));
                assemblePiece(context, facing, false,
                        vec3(8, 0, 4),
                        aabb(8, 16, 4).move(8, 0, 12),
                        cull(WEST | NORTH));
                assemblePiece(context, facing, false,
                        vec3(0, 0, 0),
                        aabb(8, 16, 8).move(0, 0, 0),
                        cull(EAST | SOUTH));
                assemblePiece(context, facing, false,
                        vec3(0, 0, 8),
                        aabb(4, 16, 8).move(0, 0, 8),
                        cull(NORTH | EAST));
                assemblePiece(context, facing, false,
                        vec3(4, 0, 8),
                        aabb(4, 16, 8).move(12, 0, 8),
                        cull(NORTH | WEST));
            }
            case INNER_LEFT -> {
                throw new AssertionError("Not yet supported");
            }
            case INNER_RIGHT -> {
                throw new AssertionError("Not yet supported");
            }
            case OUTER_LEFT -> {
                throw new AssertionError("Not yet supported");
            }
            case OUTER_RIGHT -> {
                throw new AssertionError("Not yet supported");
            }
        }
    }
}
