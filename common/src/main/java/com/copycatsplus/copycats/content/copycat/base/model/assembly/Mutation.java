package com.copycatsplus.copycats.content.copycat.base.model.assembly;


import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public record Mutation(MutationType type, int value) {
    public <T extends GlobalTransform.Transformable<T>> T mutate(T vec3) {
        return switch (type) {
            case ROTATE_X -> vec3.rotateX(value);
            case ROTATE_Y -> vec3.rotateY(value);
            case ROTATE_Z -> vec3.rotateZ(value);
            case MIRROR -> {
                if (value == 0) yield vec3.flipX(true);
                else if (value == 1) yield vec3.flipY(true);
                else if (value == 2) yield vec3.flipZ(true);
                else yield vec3;
            }
        };
    }

    public Direction mutate(Direction dir) {
        return switch (type) {
            case ROTATE_X -> rotateX(dir, value);
            case ROTATE_Y -> rotateY(dir, value);
            case ROTATE_Z -> rotateZ(dir, value);
            case MIRROR -> {
                if (value == 0 && dir.getAxis() == Axis.X) yield dir.getOpposite();
                else if (value == 1 && dir.getAxis() == Axis.Y) yield dir.getOpposite();
                else if (value == 2 && dir.getAxis() == Axis.Z) yield dir.getOpposite();
                else yield dir;
            }
        };
    }

    public <T extends GlobalTransform.Transformable<T>> T undoMutate(T vec3) {
        return switch (type) {
            case ROTATE_X -> vec3.rotateX(-value);
            case ROTATE_Y -> vec3.rotateY(-value);
            case ROTATE_Z -> vec3.rotateZ(-value);
            case MIRROR -> {
                if (value == 0) yield vec3.flipX(true);
                else if (value == 1) yield vec3.flipY(true);
                else if (value == 2) yield vec3.flipZ(true);
                else yield vec3;
            }
        };
    }

    public Direction undoMutate(Direction dir) {
        return switch (type) {
            case ROTATE_X -> rotateX(dir, -value);
            case ROTATE_Y -> rotateY(dir, -value);
            case ROTATE_Z -> rotateZ(dir, -value);
            case MIRROR -> {
                if (value == 0 && dir.getAxis() == Axis.X) yield dir.getOpposite();
                else if (value == 1 && dir.getAxis() == Axis.Y) yield dir.getOpposite();
                else if (value == 2 && dir.getAxis() == Axis.Z) yield dir.getOpposite();
                else yield dir;
            }
        };
    }

    private static Direction rotateX(Direction dir, int angle) {
        // rotate around the X axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> switch (dir) {
                case NORTH -> Direction.DOWN;
                case SOUTH -> Direction.UP;
                case UP -> Direction.NORTH;
                case DOWN -> Direction.SOUTH;
                default -> dir;
            };
            case 180 -> switch (dir) {
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                default -> dir;
            };
            case 270 -> switch (dir) {
                case NORTH -> Direction.UP;
                case SOUTH -> Direction.DOWN;
                case UP -> Direction.SOUTH;
                case DOWN -> Direction.NORTH;
                default -> dir;
            };
            default -> dir;
        };
    }

    private static Direction rotateY(Direction dir, int angle) {
        // rotate around the Y axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> switch (dir) {
                case NORTH -> Direction.EAST;
                case SOUTH -> Direction.WEST;
                case EAST -> Direction.SOUTH;
                case WEST -> Direction.NORTH;
                default -> dir;
            };
            case 180 -> switch (dir) {
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case EAST -> Direction.WEST;
                case WEST -> Direction.EAST;
                default -> dir;
            };
            case 270 -> switch (dir) {
                case NORTH -> Direction.WEST;
                case SOUTH -> Direction.EAST;
                case EAST -> Direction.NORTH;
                case WEST -> Direction.SOUTH;
                default -> dir;
            };
            default -> dir;
        };
    }

    private static Direction rotateZ(Direction dir, int angle) {
        // rotate around the Z axis clockwise
        angle = angle % 360;
        if (angle < 0) angle += 360;
        return switch (angle) {
            case 90 -> switch (dir) {
                case UP -> Direction.EAST;
                case DOWN -> Direction.WEST;
                case EAST -> Direction.DOWN;
                case WEST -> Direction.UP;
                default -> dir;
            };
            case 180 -> switch (dir) {
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
                case EAST -> Direction.WEST;
                case WEST -> Direction.EAST;
                default -> dir;
            };
            case 270 -> switch (dir) {
                case UP -> Direction.WEST;
                case DOWN -> Direction.EAST;
                case EAST -> Direction.UP;
                case WEST -> Direction.DOWN;
                default -> dir;
            };
            default -> dir;
        };
    }

    public enum MutationType {
        ROTATE_X,
        ROTATE_Y,
        ROTATE_Z,
        MIRROR
    }
}
