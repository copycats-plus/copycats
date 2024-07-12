package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatStraightPipeModelCore extends CopycatFluidPipeModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        super.registerModels(entries);
        entries.add(SUPER);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatGlassFluidPipeBlock.AXIS);

        int yRot = axis == Axis.X ? 90 : 0;
        int xRot = axis == Axis.Y ? 90 : 0;
        renderWindowCore(context, t -> t.rotateY(yRot).rotateX(xRot));
        renderWindowCore(context, t -> t.rotateZ(90).rotateY(yRot).rotateX(xRot));
        renderWindowCore(context, t -> t.rotateZ(180).rotateY(yRot).rotateX(xRot));
        renderWindowCore(context, t -> t.rotateZ(270).rotateY(yRot).rotateX(xRot));

        assembleAccessories(context);
    }

    private static final double EPSILON = 0.02;

    protected void renderWindowCore(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(
                transform,
                vec3(4 + EPSILON, 4 + EPSILON, 0),
                aabb(2, 2, 16).move(0, 0, 0),
                cull(EAST | UP | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(5, 4, 0),
                aabb(1, 1, 16).move(0, 0, 0),
                cull(EAST | WEST | DOWN | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(4, 5, 0),
                aabb(1, 1, 16).move(0, 0, 0),
                cull(WEST | UP | DOWN | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(4 + EPSILON, 6 + EPSILON, 0),
                aabb(1, 4 - 2 * EPSILON, 3).move(0, 6, 0),
                cull(UP | DOWN | NORTH)
        );
        context.assemblePiece(
                transform,
                vec3(4 + EPSILON, 6 + EPSILON, 13),
                aabb(1, 4 - 2 * EPSILON, 3).move(0, 6, 13),
                cull(UP | DOWN | SOUTH)
        );
    }
}
