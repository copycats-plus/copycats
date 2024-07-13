package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.List;
import java.util.Objects;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

public class CopycatMultiSlabModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_SLAB.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, SlabType.TOP.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.BOTTOM)
            return;
        if (Objects.equals(key, SlabType.BOTTOM.getSerializedName()) && state.getValue(CopycatSlabBlock.SLAB_TYPE) == SlabType.TOP)
            return;

        Axis axis = state.getValue(CopycatSlabBlock.AXIS);
        Direction facing = Direction.fromAxisAndDirection(axis, Objects.equals(key, SlabType.BOTTOM.getSerializedName()) ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE);

        if (facing.getAxis().isHorizontal()) {
            QuadAutoCull autoCull = autoCull(aabb(16, 16, 8));
            AssemblyTransform transform = t -> t.rotateY((int) facing.toYRot());
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 4),
                    cull(SOUTH),
                    autoCull
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 4),
                    aabb(16, 16, 4).move(0, 0, 12),
                    cull(NORTH),
                    autoCull
            );
        } else {
            QuadAutoCull autoCull = autoCull(aabb(16, 8, 16));
            AssemblyTransform transform = t -> t.flipY(facing.getAxisDirection() == AxisDirection.NEGATIVE);
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 16),
                    cull(UP),
                    autoCull
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 4, 0),
                    aabb(16, 4, 16).move(0, 12, 0),
                    cull(DOWN),
                    autoCull
            );
        }
    }
}
