package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.CCBlocks;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;

import static com.copycatsplus.copycats.content.copycat.slice.CopycatSliceBlock.HALF;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class BlockUtils {
    public static BlockState tryCopyProperties(BlockState from, BlockState to) {
        for (Property<?> property : from.getProperties()) {
            to = tryCopyProperty(from, to, property);
        }
        return to;
    }

    public static <T extends Comparable<T>> BlockState tryCopyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property) && to.hasProperty(property)) {
            return to.setValue(property, from.getValue(property));
        }
        return to;
    }

    public static Direction transformFacing(StructureTransform transform, Direction facing) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE)
            facing = transform.mirrorFacing(facing);
        if (transform.rotationAxis != null)
            facing = transform.rotateFacing(facing);
        return facing;
    }

    public static BlockState transformStepLikeHorizontal(BlockState state, StructureTransform transform, BlockState verticalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                state = state.cycle(HALF);
            } else {
                state = state.setValue(HORIZONTAL_FACING, transform.mirror.mirror(state.getValue(HORIZONTAL_FACING)));
            }
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = state.setValue(HORIZONTAL_FACING, transform.rotateFacing(state.getValue(HORIZONTAL_FACING)));
            } else {
                Direction facing = state.getValue(HORIZONTAL_FACING);
                Half half = state.getValue(HALF);
                if (transform.rotationAxis == facing.getAxis()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.cycle(HALF);
                    } else if (transform.rotation != Rotation.NONE) {
                        Direction offset = transform.rotateFacing(half == Half.TOP ? Direction.UP : Direction.DOWN);
                        boolean isClockwise = offset == facing.getClockWise();
                        state = BlockUtils.tryCopyProperties(state, verticalState)
                                .setValue(HORIZONTAL_FACING, isClockwise ? facing.getClockWise() : facing);
                    }
                } else {
                    state = setApparentDirection(state, transform.rotateFacing(getApparentDirection(state)));
                }
            }
        }
        return state;
    }

    public static BlockState transformStepLikeVertical(BlockState state, StructureTransform transform, BlockState horizontalState) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Direction.Axis mirrorAxis = null;
            for (Direction.Axis axis : Iterate.axes) {
                if (transform.mirror.rotation().inverts(axis)) {
                    mirrorAxis = axis;
                    break;
                }
            }
            if (mirrorAxis != null && !mirrorAxis.isVertical()) {
                Direction facing = state.getValue(HORIZONTAL_FACING);
                if (facing.getAxis() != mirrorAxis) {
                    state = state.setValue(HORIZONTAL_FACING, facing.getClockWise());
                } else {
                    state = state.setValue(HORIZONTAL_FACING, facing.getCounterClockWise());
                }
            }
        }
        if (transform.rotationAxis != null) {
            if (transform.rotationAxis == Direction.Axis.Y) {
                state = state.setValue(HORIZONTAL_FACING, transform.rotateFacing(state.getValue(HORIZONTAL_FACING)));
            } else {
                Direction facing = state.getValue(HORIZONTAL_FACING);
                if (facing.getAxis() == transform.rotationAxis) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.setValue(HORIZONTAL_FACING, facing.getClockWise());
                    } else if (transform.rotation != Rotation.NONE) {
                        state = BlockUtils.tryCopyProperties(state, horizontalState)
                                .setValue(HORIZONTAL_FACING, facing)
                                .setValue(HALF, (transform.rotation == Rotation.CLOCKWISE_90) == (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? Half.BOTTOM : Half.TOP);
                    }
                } else {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        state = state.setValue(HORIZONTAL_FACING, facing.getCounterClockWise());
                    } else if (transform.rotation != Rotation.NONE) {
                        state = BlockUtils.tryCopyProperties(state, horizontalState)
                                .setValue(HORIZONTAL_FACING, facing.getCounterClockWise())
                                .setValue(HALF, (transform.rotation == Rotation.CLOCKWISE_90) == (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (facing.getAxis() == Direction.Axis.X) ? Half.BOTTOM : Half.TOP);
                    }
                }
            }
        }
        return state;
    }

    public static Direction getApparentDirection(BlockState state) {
        Direction facing = state.getValue(HORIZONTAL_FACING);
        Half half = state.getValue(HALF);
        boolean positive = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        if (facing.getAxis() == Direction.Axis.X) {
            boolean aligned = positive == (half == Half.TOP);
            return aligned ? facing : positive ? Direction.DOWN : Direction.UP;
        } else {
            boolean aligned = positive == (half == Half.BOTTOM);
            return aligned ? facing : positive ? Direction.UP : Direction.DOWN;
        }
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        Direction.Axis axis = state.getValue(HORIZONTAL_FACING).getAxis();
        if (axis == Direction.Axis.X) {
            return switch (direction) {
                case UP -> state.setValue(HORIZONTAL_FACING, Direction.WEST).setValue(HALF, Half.TOP);
                case DOWN -> state.setValue(HORIZONTAL_FACING, Direction.EAST).setValue(HALF, Half.BOTTOM);
                case EAST -> state.setValue(HORIZONTAL_FACING, Direction.EAST).setValue(HALF, Half.TOP);
                case WEST -> state.setValue(HORIZONTAL_FACING, Direction.WEST).setValue(HALF, Half.BOTTOM);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        } else {
            return switch (direction) {
                case UP -> state.setValue(HORIZONTAL_FACING, Direction.SOUTH).setValue(HALF, Half.TOP);
                case DOWN -> state.setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM);
                case NORTH -> state.setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(HALF, Half.TOP);
                case SOUTH -> state.setValue(HORIZONTAL_FACING, Direction.SOUTH).setValue(HALF, Half.BOTTOM);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }
    }
}
