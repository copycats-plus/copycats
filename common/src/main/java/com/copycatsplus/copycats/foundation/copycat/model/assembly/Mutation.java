package com.copycatsplus.copycats.foundation.copycat.model.assembly;


import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

/**
 * Records a mutation to be applied to a {@link AssemblyTransform.Transformable} object or a {@link Direction}.
 *
 * @param type  The type of mutation.
 * @param value Associated value for the mutation. For rotations, this is the angle in degrees. For mirroring, this is the axis to mirror across.
 */
public record Mutation(MutationType type, int value) {
    /**
     * Apply the recorded mutation to the given object.
     */
    public <T extends AssemblyTransform.Transformable<T>> T mutate(T vec3) {
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

    /**
     * Apply the recorded mutation to the given object.
     */
    @Nullable
    @Contract("null -> null")
    public Direction mutate(@Nullable Direction dir) {
        if (dir == null) return null;
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

    /**
     * Undo the recorded mutation on the given object.
     */
    public <T extends AssemblyTransform.Transformable<T>> T undoMutate(T vec3) {
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

    /**
     * Undo the recorded mutation on the given object.
     */
    @Nullable
    @Contract("null -> null")
    public Direction undoMutate(@Nullable Direction dir) {
        if (dir == null) return null;
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

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateX(@Nullable Direction dir, int angle) {
        if (dir == null) return null;
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

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateY(@Nullable Direction dir, int angle) {
        if (dir == null) return null;
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

    @Nullable
    @Contract("null, _ -> null")
    private static Direction rotateZ(@Nullable Direction dir, int angle) {
        if (dir == null) return null;
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
