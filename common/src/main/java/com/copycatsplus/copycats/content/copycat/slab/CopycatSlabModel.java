package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.DOWN;

public class CopycatSlabModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).isPresent() ? CopycatSlabBlock.getApparentDirection(state) : Direction.UP;
        boolean isDouble = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).orElse(SlabType.BOTTOM) == SlabType.DOUBLE;

        assembleSlab(context, facing);
        if (isDouble)
            assembleSlab(context, facing.getOpposite());
    }

    private static void assembleSlab(CopycatRenderContext<?, ?> context, Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            GlobalTransform transform = t -> t.rotateY((int) facing.toYRot());
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 4),
                    cull(SOUTH)
            );
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 4),
                    aabb(16, 16, 4).move(0, 0, 12),
                    cull(NORTH)
            );
        } else {
            GlobalTransform transform = t -> t.flipY(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE);
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16),
                    cull(UP)
            );
            assemblePiece(context,
                    transform,
                    vec3(0, 4, 0),
                    aabb(16, 4, 16).move(0, 12, 0),
                    cull(DOWN)
            );
        }
    }
}
