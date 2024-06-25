package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.assembleQuad;

public class PlatformModelUtilsImpl {


    //Used in Beams
    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void quadShift(CopycatRenderContext<Source, Destination> context, Vec3i rowShiftNormal, Vec3i columnShiftNormal, AABB bb1, Vec3 offset) {
        Direction direction = context.source().lightFace();

        if (rowShiftNormal.equals(direction.getNormal()))
            return;
        if (columnShiftNormal.equals(direction.getNormal()))
            return;

        assembleQuad(context, bb1, offset);
    }


    //Used in Vertical Step
    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleVerticalStep(CopycatRenderContext<Source, Destination> context, boolean row, boolean column, AABB bb1, Vec3 offset) {
        Direction direction = context.source().lightFace();

        if (direction.getAxis() == Direction.Axis.X && row == (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE))
            return;
        if (direction.getAxis() == Direction.Axis.Z && column == (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE))
            return;

        assembleQuad(context, bb1, offset);
    }

}
