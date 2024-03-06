package com.copycatsplus.copycats.content.copycat.vertical_step;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;

public class CopycatVerticalStepModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getOptionalValue(CopycatVerticalStepBlock.FACING).orElse(Direction.NORTH);
        Direction perpendicular = facing.getCounterClockWise();

        int xOffset = (facing.getAxis() == Direction.Axis.X ? facing : perpendicular).getAxisDirection().getStep();
        int zOffset = (facing.getAxis() == Direction.Axis.Z ? facing : perpendicular).getAxisDirection().getStep();
        Vec3 rowNormal = new Vec3(1, 0, 0);
        Vec3 columnNormal = new Vec3(0, 0, 1);
        AABB bb = CUBE_AABB.contract(12 / 16.0, 0, 12 / 16.0);
        // 4 Pieces
        for (boolean row : Iterate.trueAndFalse) {
            for (boolean column : Iterate.trueAndFalse) {

                AABB bb1 = bb;
                if (row)
                    bb1 = bb1.move(rowNormal.scale(12 / 16.0));
                if (column)
                    bb1 = bb1.move(columnNormal.scale(12 / 16.0));

                Vec3 offset = Vec3.ZERO;
                Vec3 rowShift = rowNormal.scale(row
                        ? 8 * Mth.clamp(xOffset, -1, 0) / 16.0
                        : 8 * Mth.clamp(xOffset, 0, 1) / 16.0
                );
                Vec3 columnShift = columnNormal.scale(column
                        ? 8 * Mth.clamp(zOffset, -1, 0) / 16.0
                        : 8 * Mth.clamp(zOffset, 0, 1) / 16.0
                );
                offset = offset.add(rowShift);
                offset = offset.add(columnShift);

                assembleVerticalStep(context, row, column, bb1, offset);
            }
        }
    }

}
