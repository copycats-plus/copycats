package com.copycatsplus.copycats.util;

import net.minecraft.core.Direction;

public class DirectionUtils {
    public static Direction fromDelta(int pX, int pY, int pZ) {
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
