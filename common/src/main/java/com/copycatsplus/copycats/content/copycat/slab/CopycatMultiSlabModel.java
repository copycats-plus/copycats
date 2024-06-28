package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

public class CopycatMultiSlabModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        if (Objects.equals(key, SlabType.TOP.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.BOTTOM)
            return;
        if (Objects.equals(key, SlabType.BOTTOM.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.TOP)
            return;

        Axis axis = state.getValue(CopycatSlabBlock.AXIS);
        Direction facing = Direction.fromAxisAndDirection(axis, Objects.equals(key, SlabType.BOTTOM.getSerializedName()) ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);

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
            GlobalTransform transform = t -> t.flipY(facing.getAxisDirection() == AxisDirection.NEGATIVE);
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
