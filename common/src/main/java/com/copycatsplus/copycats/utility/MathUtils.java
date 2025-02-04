package com.copycatsplus.copycats.utility;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MathUtils {
    public static Vec3i replaceAxis(Vec3i vec, Direction.Axis axis, int value) {
        return switch (axis) {
            case X -> new Vec3i(value, vec.getY(), vec.getZ());
            case Y -> new Vec3i(vec.getX(), value, vec.getZ());
            case Z -> new Vec3i(vec.getX(), vec.getY(), value);
        };
    }

    public static Vec3 replaceAxis(Vec3 vec, Direction.Axis axis, double value) {
        return switch (axis) {
            case X -> new Vec3(value, vec.y(), vec.z());
            case Y -> new Vec3(vec.x(), value, vec.z());
            case Z -> new Vec3(vec.x(), vec.y(), value);
        };
    }

    public static Vec3 clampToGrid(Vec3 vec, Vec3i pos) {
        return new Vec3(
                Mth.clamp(vec.x, pos.getX(), pos.getX() + 1),
                Mth.clamp(vec.y, pos.getY(), pos.getY() + 1),
                Mth.clamp(vec.z, pos.getZ(), pos.getZ() + 1)
        );
    }
}
