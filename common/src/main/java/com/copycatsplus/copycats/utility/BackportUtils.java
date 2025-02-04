package com.copycatsplus.copycats.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

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

    public static <T extends Comparable<T>> BlockState trySetValue(BlockState state, Property<T> property, T value) {
        return state.hasProperty(property) ? state.setValue(property, value) : state;
    }

    public static class Vector2i {
        public int x;
        public int y;

        public Vector2i(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
