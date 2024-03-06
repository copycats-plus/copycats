package com.copycatsplus.copycats.content.copycat.base.model.forge;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

//Not sure why but if both aren't imported it errors
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.forge.QuadHelperImpl.*;

public class PlatformModelUtilsImpl {


    //Used in Slabs
    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void cullFacing(Direction facing, CopycatRenderContext<Source, Destination> context, boolean front, boolean topSlab, boolean isDouble, AABB bb, Vec3 normalScaledN8) {
        for (int i = 0; i < context.source().size(); i++) {
            BakedQuad quad = context.source().get(i);
            Direction direction = quad.getDirection();

            if (front && direction == facing)
                continue;
            if (!front && direction == facing.getOpposite())
                continue;
            if (isDouble && topSlab && direction == facing)
                continue;
            if (isDouble && !topSlab && direction == facing.getOpposite())
                continue;

            assembleQuad(quad, context.destination(), bb, normalScaledN8);
        }
    }


    //Used in Beams
    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void quadShift(CopycatRenderContext<Source, Destination> context, Vec3i rowShiftNormal, Vec3i columnShiftNormal, AABB bb1, Vec3 offset) {
        for (int i = 0; i < context.source().size(); i++) {
            BakedQuad quad = context.source().get(i);
            Direction direction = quad.getDirection();

            if (rowShiftNormal.equals(direction.getNormal()))
                continue;
            if (columnShiftNormal.equals(direction.getNormal()))
                continue;

            assembleQuad(quad, context.destination(), bb1, offset);
        }
    }


    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assembleVerticalStep(CopycatRenderContext<Source, Destination> context, boolean row, boolean column, AABB bb1, Vec3 offset) {
        for (int i = 0; i < context.source().size(); i++) {
            BakedQuad quad = context.source().get(i);
            Direction direction = quad.getDirection();

            if (direction.getAxis() == Direction.Axis.X && row == (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE))
                continue;
            if (direction.getAxis() == Direction.Axis.Z && column == (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE))
                continue;

            assembleQuad(quad, context.destination(), bb1, offset);
        }
    }

}
