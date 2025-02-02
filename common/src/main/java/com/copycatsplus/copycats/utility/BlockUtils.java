package com.copycatsplus.copycats.utility;

import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

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

    /**
     * Transform a block state that has 8 possible orientations at the 8 corners of a cube, represented by the
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#HORIZONTAL_FACING} and
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#HALF} properties.
     * When the facing of the block matches the facing of viewing perspective, the block should be located at the far left corner.
     */
    public static BlockState transformCornerLike(BlockState state, StructureTransform transform) {
        return vecToCorner(state, transform.applyWithoutOffset(cornerToVec(state)));
    }

    public static Vec3 cornerToVec(BlockState state) {
        Direction facing = state.getValue(HORIZONTAL_FACING);
        double x = facing == Direction.EAST || facing == Direction.SOUTH ? 1 : 0;
        double z = facing == Direction.SOUTH || facing == Direction.WEST ? 1 : 0;
        double y = state.getValue(HALF) == Half.TOP ? 1 : 0;
        return new Vec3(x, y, z);
    }

    public static BlockState vecToCorner(BlockState state, Vec3 vec) {
        Direction facing = vec.x > 0.5 ? vec.z > 0.5 ? Direction.SOUTH : Direction.EAST : vec.z > 0.5 ? Direction.WEST : Direction.NORTH;
        Half half = vec.y > 0.5 ? Half.TOP : Half.BOTTOM;
        return state.setValue(HORIZONTAL_FACING, facing).setValue(HALF, half);
    }

    /**
     * Transforms a block state that has 8 possible orientations at the 8 horizontal edges of a cube, represented by the
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#HORIZONTAL_FACING} and
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#HALF} properties.
     */
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

    /**
     * Transforms a block state that has 4 possible orientations at the 4 vertical edges of a cube, represented by the
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#HORIZONTAL_FACING} property.
     * When the facing of the block matches the facing of viewing perspective, the block should be located at the far left corner.
     */
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
