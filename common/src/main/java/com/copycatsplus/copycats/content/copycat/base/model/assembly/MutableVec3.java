package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MutableVec3 implements GlobalTransform.Transformable<MutableVec3>, Position {
    public double x;
    public double y;
    public double z;

    public MutableVec3(Position position) {
        this(position.x(), position.y(), position.z());
    }

    public MutableVec3(double x, double y, double z) {
        set(x, y, z);
    }

    public MutableVec3 rotateY(int angle) {
        // rotate around the Y axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(1 - z, y, x);
            case 180 -> set(1 - x, y, 1 - z);
            case 270 -> set(z, y, 1 - x);
            default -> this;
        };
    }

    public MutableVec3 rotateX(int angle) {
        // rotate around the X axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(x, z, 1 - y);
            case 180 -> set(x, 1 - y, 1 - z);
            case 270 -> set(x, 1 - z, y);
            default -> this;
        };
    }

    public MutableVec3 rotateZ(int angle) {
        // rotate around the Z axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(y, 1 - x, z);
            case 180 -> set(1 - x, 1 - y, z);
            case 270 -> set(1 - y, x, z);
            default -> this;
        };
    }

    public MutableVec3 flipX(boolean flip) {
        if (!flip) return this;
        return set(1 - x, y, z);
    }

    public MutableVec3 flipY(boolean flip) {
        if (!flip) return this;
        return set(x, 1 - y, z);
    }

    public MutableVec3 flipZ(boolean flip) {
        if (!flip) return this;
        return set(x, y, 1 - z);
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, z);
    }

    public MutableVec3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double get(Axis axis) {
        return switch (axis) {
            case X -> x;
            case Y -> y;
            case Z -> z;
        };
    }

    public MutableVec3 set(Axis axis, double value) {
        switch (axis) {
            case X -> x = value;
            case Y -> y = value;
            case Z -> z = value;
        }
        return this;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public MutableVec3 add(Position vec) {
        return add(vec.x(), vec.y(), vec.z());
    }

    public MutableVec3 add(double x, double y, double z) {
        return set(this.x + x, this.y + y, this.z + z);
    }

    public MutableVec3 subtract(Position vec) {
        return set(x - vec.x(), y - vec.y(), z - vec.z());
    }

    public MutableVec3 scale(double scale) {
        return set(x * scale, y * scale, z * scale);
    }

    public MutableVec3 multiply(Position vec) {
        return set(x * vec.x(), y * vec.y(), z * vec.z());
    }

    public MutableVec3 rotate(Position rotationVec) {
        return rotate(rotationVec.x(), rotationVec.y(), rotationVec.z());
    }

    public MutableVec3 rotate(double xRot, double yRot, double zRot) {
        rotate(xRot, Axis.X);
        rotate(yRot, Axis.Y);
        rotate(zRot, Axis.Z);
        return this;
    }

    public MutableVec3 rotate(double deg, Axis axis) {
        if (deg == 0)
            return this;
        if (this.isZero())
            return this;

        float angle = (float) (deg / 180f * Math.PI);
        double sin = Mth.sin(angle);
        double cos = Mth.cos(angle);
        double x = this.x;
        double y = this.y;
        double z = this.z;

        if (axis == Axis.X) {
            this.y = y * cos - z * sin;
            this.z = z * cos + y * sin;
        } else if (axis == Axis.Y) {
            this.x = x * cos + z * sin;
            this.z = z * cos - x * sin;
        } else if (axis == Axis.Z) {
            this.x = x * cos - y * sin;
            this.y = y * cos + x * sin;
        }
        return this;
    }

    public double dot(Position vec) {
        return this.x * vec.x() + this.y * vec.y() + this.z * vec.z();
    }

    /**
     * Returns a new vector with the result of this vector x the specified vector.
     */
    public MutableVec3 cross(Position vec) {
        return set(this.y * vec.z() - this.z * vec.y(), this.z * vec.x() - this.x * vec.z(), this.x * vec.y() - this.y * vec.x());
    }


    /**
     * Euclidean distance between this and the specified vector, returned as double.
     */
    public double distanceTo(Position vec) {
        double d = vec.x() - this.x;
        double e = vec.y() - this.y;
        double f = vec.z() - this.z;
        return Math.sqrt(d * d + e * e + f * f);
    }

    /**
     * The square of the Euclidean distance between this and the specified vector.
     */
    public double distanceToSqr(Position vec) {
        double d = vec.x() - this.x;
        double e = vec.y() - this.y;
        double f = vec.z() - this.z;
        return d * d + e * e + f * f;
    }

    public double distanceToSqr(double x, double y, double z) {
        double d = x - this.x;
        double e = y - this.y;
        double f = z - this.z;
        return d * d + e * e + f * f;
    }

    /**
     * Returns the length of the vector.
     */
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public double horizontalDistance() {
        return Math.sqrt(this.x * this.x + this.z * this.z);
    }

    public double horizontalDistanceSqr() {
        return this.x * this.x + this.z * this.z;
    }

    /**
     * Normalizes the vector to a length of 1 (except if it is the zero vector)
     */
    public MutableVec3 normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (d < 1.0E-4) {
            return set(0, 0, 0);
        }
        return set(this.x / d, this.y / d, this.z / d);
    }

    public MutableVec3 copy() {
        return new MutableVec3(x, y, z);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    /**
     * Marker subclass for when a Vec3 is used as a pivot point
     */
    public static class AsPivot extends MutableVec3 {
        public AsPivot(double x, double y, double z) {
            super(x, y, z);
        }
    }

    /**
     * Marker subclass for when a Vec3 is used as axis-angle rotation
     */
    public static class AsAngle extends MutableVec3 {
        public AsAngle(double x, double y, double z) {
            super(x, y, z);
        }
    }

    /**
     * Marker subclass for when a Vec3 is used as a scale factor
     */
    public static class AsScale extends MutableVec3 {
        public AsScale(double x, double y, double z) {
            super(x, y, z);
        }
    }
}
