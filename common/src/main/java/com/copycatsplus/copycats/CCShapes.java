package com.copycatsplus.copycats;

import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.core.Direction.UP;

public class CCShapes {
    public static final VoxelShaper CASING_1PX = shape(0, 15, 0, 16, 16, 16).forDirectional();
    public static final VoxelShaper CASING_8PX = shape(0, 0, 0, 16, 8, 16).forAxis();
    public static final VoxelShaper CASING_8PX_TOP = shape(0, 8, 0, 16, 16, 16).forAxis();
    public static final VoxelShaper CASING_8PX_CENTERED = shape(4, 0, 4, 12, 16, 12).forAxis();
    public static final VoxelShaper CASING_8PX_VERTICAL = shape(0, 0, 0, 8, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper EMPTY = shape(0.0, 0.0, 0.0, 0, 0, 0).forDirectional();
    public static final VoxelShaper LAYER_2PX = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_4PX = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_6PX = shape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_8PX = shape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_10PX = shape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_12PX = shape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_14PX = shape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_16PX = shape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0).forDirectional();
    public static final VoxelShaper HALF_PANEL_NORTH = shape(0.0, 0.0, 0.0, 16.0, 3.0, 8.0).forOffsetDirectional(Direction.DOWN, Direction.NORTH);
    public static final VoxelShaper HALF_PANEL_SOUTH = shape(0.0, 0.0, 8.0, 16.0, 3.0, 16.0).forOffsetDirectional(Direction.DOWN, Direction.SOUTH);
    public static final VoxelShaper HALF_PANEL_EAST = shape(8.0, 0.0, 0.0, 16.0, 3.0, 16.0).forOffsetDirectional(Direction.DOWN, Direction.EAST);
    public static final VoxelShaper HALF_PANEL_WEST = shape(0.0, 0.0, 0.0, 8.0, 3.0, 16.0).forOffsetDirectional(Direction.DOWN, Direction.WEST);
    public static final VoxelShaper SLICE_BOTTOM_2PX = shape(0, 0, 14, 16, 2, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_4PX = shape(0, 0, 12, 16, 4, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_6PX = shape(0, 0, 10, 16, 6, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_8PX = shape(0, 0, 8, 16, 8, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_10PX = shape(0, 0, 6, 16, 10, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_12PX = shape(0, 0, 4, 16, 12, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_14PX = shape(0, 0, 2, 16, 14, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_BOTTOM_16PX = shape(0, 0, 0, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_2PX = shape(0, 14, 14, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_4PX = shape(0, 12, 12, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_6PX = shape(0, 10, 10, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_8PX = shape(0, 8, 8, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_10PX = shape(0, 6, 6, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_12PX = shape(0, 4, 4, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_14PX = shape(0, 2, 2, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_TOP_16PX = shape(0, 0, 0, 16, 16, 16).forHorizontal(Direction.SOUTH);
    public static final VoxelShaper SLICE_VERTICAL_2PX = shape(0, 0, 0, 2, 16, 2).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_4PX = shape(0, 0, 0, 4, 16, 4).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_6PX = shape(0, 0, 0, 6, 16, 6).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_8PX = shape(0, 0, 0, 8, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_10PX = shape(0, 0, 0, 10, 16, 10).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_12PX = shape(0, 0, 0, 12, 16, 12).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_14PX = shape(0, 0, 0, 14, 16, 14).forHorizontal(Direction.NORTH);
    public static final VoxelShaper SLICE_VERTICAL_16PX = shape(0, 0, 0, 16, 16, 16).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_2PX = shape(0, 0, 0, 16, 2, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_4PX = shape(0, 0, 0, 16, 4, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_6PX = shape(0, 0, 0, 16, 6, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_8PX = shape(0, 0, 0, 16, 8, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_10PX = shape(0, 0, 0, 16, 10, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_12PX = shape(0, 0, 0, 16, 12, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_14PX = shape(0, 0, 0, 16, 14, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_BOTTOM_16PX = shape(0, 0, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_2PX = shape(0, 14, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_4PX = shape(0, 12, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_6PX = shape(0, 10, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_8PX = shape(0, 8, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_10PX = shape(0, 6, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_12PX = shape(0, 4, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_14PX = shape(0, 2, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper HALF_LAYER_TOP_16PX = shape(0, 0, 0, 16, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper VERTICAL_STAIR = shape(0, 0, 8, 16, 16, 16)
            .add(8, 0, 0, 16, 16, 8).forDirectional(Direction.NORTH);
    private static Builder shape(VoxelShape shape) {
        return new Builder(shape);
    }

    private static Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }

        public VoxelShaper build(TriFunction<VoxelShape, Direction, Direction, VoxelShaper> factory, Direction direction, Direction offset) {
            return factory.apply(shape, direction, offset);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forOffsetDirectional(Direction direction, Direction offset) {
            return build(VoxelShaper::forOffsetDirectional, direction, offset);
        }

        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }

        public VoxelShaper forOffsetDirectional(Direction offset) {
            return forOffsetDirectional(UP, offset);
        }

    }

    public static class VoxelShaper extends com.simibubi.create.foundation.utility.VoxelShaper {

        private Map<Direction, VoxelShape> shapes = new HashMap<>();

        public VoxelShape get(Direction direction) {
            return shapes.get(direction);
        }

        public VoxelShape get(Direction.Axis axis) {
            return shapes.get(axisAsFace(axis));
        }

        public static VoxelShaper forVertical(VoxelShape shape, Direction facing) {
            return forDirectionsWithRotation(shape, facing, Direction.Plane.VERTICAL, new VoxelShaper.DefaultRotationValues());
        }

        public static VoxelShaper forHorizontal(VoxelShape shape, Direction facing) {
            return forDirectionsWithRotation(shape, facing, Direction.Plane.HORIZONTAL, new VoxelShaper.HorizontalRotationValues());
        }

        public static VoxelShaper forHorizontalAxis(VoxelShape shape, Direction.Axis along) {
            return forDirectionsWithRotation(shape, axisAsFace(along), Arrays.asList(Direction.SOUTH, Direction.EAST),
                    new VoxelShaper.HorizontalRotationValues());
        }

        public static VoxelShaper forDirectional(VoxelShape shape, Direction facing) {
            return forDirectionsWithRotation(shape, facing, Arrays.asList(Iterate.directions), new VoxelShaper.DefaultRotationValues());
        }

        public static VoxelShaper forOffsetDirectional(VoxelShape shape, Direction facing, Direction offset) {
            return forDirectionsWithRotation(shape, facing, Arrays.asList(Iterate.directions), new OffsetRotationValues(offset));
        }

        public static VoxelShaper forAxis(VoxelShape shape, Direction.Axis along) {
            return forDirectionsWithRotation(shape, axisAsFace(along),
                    Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.UP), new VoxelShaper.DefaultRotationValues());
        }

        public VoxelShaper withVerticalShapes(VoxelShape upShape) {
            shapes.put(Direction.UP, upShape);
            shapes.put(Direction.DOWN, rotatedCopy(upShape, new Vec3(180, 0, 0)));
            return this;
        }

        public VoxelShaper withShape(VoxelShape shape, Direction facing) {
            shapes.put(facing, shape);
            return this;
        }

        public static Direction axisAsFace(Direction.Axis axis) {
            return Direction.get(Direction.AxisDirection.POSITIVE, axis);
        }

        protected static float horizontalAngleFromDirection(Direction direction) {
            return (float) ((Math.max(direction.get2DDataValue(), 0) & 3) * 90);
        }

        protected static VoxelShaper forDirectionsWithRotation(VoxelShape shape, Direction facing,
                                                               Iterable<Direction> directions, Function<Direction, Vec3> rotationValues) {
            VoxelShaper voxelShaper = new VoxelShaper();
            for (Direction dir : directions) {
                voxelShaper.shapes.put(dir, rotate(shape, facing, dir, rotationValues));
            }
            return voxelShaper;
        }

        protected static VoxelShape rotate(VoxelShape shape, Direction from, Direction to,
                                           Function<Direction, Vec3> usingValues) {
            if (from == to)
                return shape;

            return rotatedCopy(shape, usingValues.apply(from)
                    .reverse()
                    .add(usingValues.apply(to)));
        }

        protected static VoxelShape rotatedCopy(VoxelShape shape, Vec3 rotation) {
            if (rotation.equals(Vec3.ZERO))
                return shape;

            MutableObject<VoxelShape> result = new MutableObject<>(Shapes.empty());
            Vec3 center = new Vec3(8, 8, 8);

            shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
                Vec3 v1 = new Vec3(x1, y1, z1).scale(16)
                        .subtract(center);
                Vec3 v2 = new Vec3(x2, y2, z2).scale(16)
                        .subtract(center);

                v1 = VecHelper.rotate(v1, (float) rotation.x, Direction.Axis.X);
                v1 = VecHelper.rotate(v1, (float) rotation.y, Direction.Axis.Y);
                v1 = VecHelper.rotate(v1, (float) rotation.z, Direction.Axis.Z)
                        .add(center);

                v2 = VecHelper.rotate(v2, (float) rotation.x, Direction.Axis.X);
                v2 = VecHelper.rotate(v2, (float) rotation.y, Direction.Axis.Y);
                v2 = VecHelper.rotate(v2, (float) rotation.z, Direction.Axis.Z)
                        .add(center);

                VoxelShape rotated = blockBox(v1, v2);
                result.setValue(Shapes.or(result.getValue(), rotated));
            });

            return result.getValue();
        }

        protected static VoxelShape blockBox(Vec3 v1, Vec3 v2) {
            return Block.box(
                    Math.min(v1.x, v2.x),
                    Math.min(v1.y, v2.y),
                    Math.min(v1.z, v2.z),
                    Math.max(v1.x, v2.x),
                    Math.max(v1.y, v2.y),
                    Math.max(v1.z, v2.z)
            );
        }

        protected static class OffsetRotationValues implements Function<Direction, Vec3> {
            private final Direction offset;

            public OffsetRotationValues(Direction offset) {
                this.offset = offset;
            }

            // assume facing up as the default rotation
            @Override
            public Vec3 apply(Direction direction) {
                boolean positive = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                if (direction.getAxis() != offset.getAxis())
                    return switch (direction.getAxis()) {
                        case X -> new Vec3(0, 0, positive ? -90 : 90);
                        case Y -> offset.getAxis() == Direction.Axis.X
                                ? new Vec3(0, positive ? 0 : 180, positive ? 0 : 180)
                                : new Vec3(positive ? 0 : 180, positive ? 0 : 180, 0);
                        case Z -> new Vec3(positive ? 90 : -90, 0, 0);
                    };
                else
                    return switch (direction.getAxis()) {
                        case X ->
                                new Vec3(0, direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 180 : 0, positive ? -90 : 90);
                        case Y -> new Vec3(positive ? 0 : 180, positive ? 0 : 180, 0);
                        case Z ->
                                new Vec3(positive ? -90 : 90, 0, direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 180 : 0);
                    };
            }
        }

        protected static class DefaultRotationValues implements Function<Direction, Vec3> {
            // assume facing up as the default rotation
            @Override
            public Vec3 apply(Direction direction) {
                return new Vec3(direction == Direction.UP ? 0 : (Direction.Plane.VERTICAL.test(direction) ? 180 : 90),
                        -horizontalAngleFromDirection(direction), 0);
            }
        }

        protected static class HorizontalRotationValues implements Function<Direction, Vec3> {
            @Override
            public Vec3 apply(Direction direction) {
                return new Vec3(0, -horizontalAngleFromDirection(direction), 0);
            }
        }

    }
}
