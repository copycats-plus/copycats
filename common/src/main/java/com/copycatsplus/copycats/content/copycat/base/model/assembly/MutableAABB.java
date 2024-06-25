package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.world.phys.AABB;

public class MutableAABB implements GlobalTransform.Transformable<MutableAABB> {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public MutableAABB(double sizeX, double sizeY, double sizeZ) {
        set(0, 0, 0, sizeX, sizeY, sizeZ);
    }

    /**
     * Move the entire AABB by the given amount in VOXEL SPACE
     */
    public MutableAABB move(double dX, double dY, double dZ) {
        dX /= 16;
        dY /= 16;
        dZ /= 16;
        minX += dX;
        maxX += dX;
        minY += dY;
        maxY += dY;
        minZ += dZ;
        maxZ += dZ;
        return this;
    }

    /**
     * Move the entire AABB by the given amount in BLOCK SPACE
     */
    public MutableAABB shift(double dX, double dY, double dZ) {
        minX += dX;
        maxX += dX;
        minY += dY;
        maxY += dY;
        minZ += dZ;
        maxZ += dZ;
        return this;
    }

    public MutableAABB rotateY(int angle) {
        // rotate around the Y axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(1 - minZ, minY, minX, 1 - maxZ, maxY, maxX);
            case 180 -> set(1 - minX, minY, 1 - minZ, 1 - maxX, maxY, 1 - maxZ);
            case 270 -> set(minZ, minY, 1 - minX, maxZ, maxY, 1 - maxX);
            default -> this;
        };
    }

    public MutableAABB rotateX(int angle) {
        // rotate around the X axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(minX, minZ, 1 - minY, maxX, maxZ, 1 - maxY);
            case 180 -> set(minX, 1 - minY, 1 - minZ, maxX, 1 - maxY, 1 - maxZ);
            case 270 -> set(minX, 1 - minZ, minY, maxX, 1 - maxZ, maxY);
            default -> this;
        };
    }

    public MutableAABB rotateZ(int angle) {
        // rotate around the Z axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(minY, 1 - minX, minZ, maxY, 1 - maxX, maxZ);
            case 180 -> set(1 - minX, 1 - minY, minZ, 1 - maxX, 1 - maxY, maxZ);
            case 270 -> set(1 - minY, minX, minZ, 1 - maxY, maxX, maxZ);
            default -> this;
        };
    }

    public MutableAABB flipX(boolean flip) {
        if (!flip) return this;
        return set(1 - minX, minY, minZ, 1 - maxX, maxY, maxZ);
    }

    public MutableAABB flipY(boolean flip) {
        if (!flip) return this;
        return set(minX, 1 - minY, minZ, maxX, 1 - maxY, maxZ);
    }

    public MutableAABB flipZ(boolean flip) {
        if (!flip) return this;
        return set(minX, minY, 1 - minZ, maxX, maxY, 1 - maxZ);
    }

    public AABB toAABB() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public MutableAABB set(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }
}
