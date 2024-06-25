package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import net.minecraft.core.Direction;

public class MutableCullFace implements GlobalTransform.Transformable<MutableCullFace> {

    public static final int UP = 2 << Direction.UP.get3DDataValue();
    public static final int DOWN = 2 << Direction.DOWN.get3DDataValue();
    public static final int NORTH = 2 << Direction.NORTH.get3DDataValue();
    public static final int EAST = 2 << Direction.EAST.get3DDataValue();
    public static final int SOUTH = 2 << Direction.SOUTH.get3DDataValue();
    public static final int WEST = 2 << Direction.WEST.get3DDataValue();

    public boolean up;
    public boolean down;
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;

    public MutableCullFace(int mask) {
        set((mask & UP) > 0, (mask & DOWN) > 0, (mask & NORTH) > 0, (mask & SOUTH) > 0, (mask & EAST) > 0, (mask & WEST) > 0);
    }

    public MutableCullFace rotateY(int angle) {
        // rotate around the Y axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(up, down, west, east, north, south);
            case 180 -> set(up, down, south, north, west, east);
            case 270 -> set(up, down, east, west, south, north);
            default -> this;
        };
    }

    public MutableCullFace rotateX(int angle) {
        // rotate around the X axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(south, north, up, down, east, west);
            case 180 -> set(down, up, south, north, east, west);
            case 270 -> set(north, south, down, up, east, west);
            default -> this;
        };
    }

    public MutableCullFace rotateZ(int angle) {
        // rotate around the Z axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> set(west, east, north, south, up, down);
            case 180 -> set(down, up, north, south, west, east);
            case 270 -> set(east, west, north, south, down, up);
            default -> this;
        };
    }

    public MutableCullFace flipX(boolean flip) {
        if (!flip) return this;
        return set(up, down, north, south, west, east);
    }

    public MutableCullFace flipY(boolean flip) {
        if (!flip) return this;
        return set(down, up, north, south, east, west);
    }

    public MutableCullFace flipZ(boolean flip) {
        if (!flip) return this;
        return set(up, down, south, north, east, west);
    }

    public boolean isCulled(Direction direction) {
        return switch (direction) {
            case DOWN -> down;
            case UP -> up;
            case NORTH -> north;
            case SOUTH -> south;
            case WEST -> west;
            case EAST -> east;
        };
    }

    public MutableCullFace set(boolean up, boolean down, boolean north, boolean south, boolean east, boolean west) {
        this.up = up;
        this.down = down;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        return this;
    }
}
