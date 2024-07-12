package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatSlabModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).isPresent() ? CopycatSlabBlock.getApparentDirection(state) : Direction.UP;
        boolean isDouble = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).orElse(SlabType.BOTTOM) == SlabType.DOUBLE;

        assembleSlab(context, facing);
        if (isDouble)
            assembleSlab(context, facing.getOpposite());
    }

    private static void assembleSlab(CopycatRenderContext context, Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            AssemblyTransform transform = t -> t.rotateY((int) facing.toYRot());
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 4),
                    cull(SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 4),
                    aabb(16, 16, 4).move(0, 0, 12),
                    cull(NORTH)
            );
        } else {
            AssemblyTransform transform = t -> t.flipY(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(16, 4, 16).move(0, 12, 0),
                    cull(DOWN)
            );
        }
    }
}
