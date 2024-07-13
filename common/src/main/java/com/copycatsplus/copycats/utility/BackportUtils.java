package com.copycatsplus.copycats.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;

public class BackportUtils {
    public static BlockPos blockPosContaining(Position pos) {
        return new BlockPos(Mth.floor(pos.x()), Mth.floor(pos.y()), Mth.floor(pos.z()));
    }

    public static Direction directionFromDelta(int pX, int pY, int pZ) {
        if (pX == 0) {
            if (pY == 0) {
                if (pZ > 0) {
                    return Direction.SOUTH;
                }

                if (pZ < 0) {
                    return Direction.NORTH;
                }
            } else if (pZ == 0) {
                if (pY > 0) {
                    return Direction.UP;
                }

                return Direction.DOWN;
            }
        } else if (pY == 0 && pZ == 0) {
            if (pX > 0) {
                return Direction.EAST;
            }

            return Direction.WEST;
        }

        return null;
    }
}
