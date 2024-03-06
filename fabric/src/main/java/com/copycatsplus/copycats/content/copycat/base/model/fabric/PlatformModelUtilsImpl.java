package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.assembleQuad;

public class PlatformModelUtilsImpl {


    //Used in Slabs
    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void cullFacing(Direction facing, CopycatRenderContext<Source, Destination> context, boolean front, boolean topSlab, boolean isDouble, AABB bb, Vec3 normalScaledN8) {

        Direction direction = context.source().lightFace();

        if (front && direction == facing)
            return;
        if (!front && direction == facing.getOpposite())
            return;
        if (isDouble && topSlab && direction == facing)
            return;
        if (isDouble && !topSlab && direction == facing.getOpposite())
            return;

        assembleQuad(context, bb, normalScaledN8);
    }


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
