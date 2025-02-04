package com.copycatsplus.copycats.foundation.copycat;

import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A representation of the {@link net.minecraft.world.level.block.state.BlockState} of a copycat that is
 * much easier to transform.
 */
public class CopycatTransformableState<T> {
    public List<Part<T>> parts = new LinkedList<>();

    public static <T> CopycatTransformableState<T> create(Consumer<CopycatTransformableState<T>> consumer) {
        CopycatTransformableState<T> state = new CopycatTransformableState<>();
        consumer.accept(state);
        return state;
    }

    public Part<T> addPart(int x, int y, int z) {
        Part<T> part = new Part<>(x, y, z);
        parts.add(part);
        return part;
    }

    public CopycatTransformableState<T> transform(StructureTransform transform) {
        parts.forEach(part -> part.transform(transform));
        return this;
    }

    public CopycatTransformableState<T> untransform(StructureTransform transform) {
        parts.forEach(part -> part.untransform(transform));
        return this;
    }

    public static class Part<T> {
        /**
         * Vector in voxel space that represents the position of the part.
         */
        public Vec3i vector;
        /**
         * Data that is associated with the part.
         */
        public T data = null;

        public Part(int x, int y, int z) {
            vector = new Vec3i(x, y, z);
        }

        public Part<T> setData(T data) {
            this.data = data;
            return this;
        }

        public T getData() {
            return data;
        }

        /**
         * Rotates the part around the y-axis, assuming that the current facing is "origin".
         *
         * @param facing The new facing of the part.
         * @param origin The current facing of the part.
         * @return The part itself.
         */
        public Part<T> rotateY(Direction facing, Direction origin) {
            // toYRot is clockwise but StructureTransform uses counter-clockwise rotation
            StructureTransform transform = new StructureTransform(BlockPos.ZERO, 0, origin.toYRot() - facing.toYRot(), 0);
            return transform(transform);
        }

        /**
         * Rotates the part around the y-axis, assuming that the current facing is south.
         *
         * @param facing The new facing of the part.
         * @return The part itself.
         */
        public Part<T> rotateY(Direction facing) {
            return rotateY(facing, Direction.SOUTH);
        }

        public Part<T> transform(StructureTransform transform) {
            Vec3 transformed = transform.applyWithoutOffset(new Vec3(vector.getX() / 16d, vector.getY() / 16d, vector.getZ() / 16d));
            vector = new Vec3i((int) Math.round(transformed.x * 16), (int) Math.round(transformed.y * 16), (int) Math.round(transformed.z * 16));
            return this;
        }

        /**
         * Equivalent to {@link StructureTransform#unapplyWithoutOffset(Vec3)} but reimplemented to avoid crash in Create 0.5.1f.
         */
        private static Vec3 unapplyWithoutOffset(StructureTransform transform, Vec3 globalVec) {
            Vec3 vec = globalVec;
            if (transform.rotationAxis != null)
                vec = VecHelper.rotateCentered(vec, -transform.angle, transform.rotationAxis);
            if (transform.mirror != null)
                vec = VecHelper.mirrorCentered(vec, transform.mirror);

            return vec;
        }

        public Part<T> untransform(StructureTransform transform) {
            Vec3 transformed = unapplyWithoutOffset(transform, new Vec3(vector.getX() / 16d, vector.getY() / 16d, vector.getZ() / 16d));
            vector = new Vec3i((int) Math.round(transformed.x * 16), (int) Math.round(transformed.y * 16), (int) Math.round(transformed.z * 16));
            return this;
        }

        public Direction getFacing() {
            int dx = Math.abs(vector.getX() - 8);
            int dy = Math.abs(vector.getY() - 8);
            int dz = Math.abs(vector.getZ() - 8);
            if (dz > dx && dz > dy) {
                return vector.getZ() > 8 ? Direction.SOUTH : Direction.NORTH;
            }
            if (dx > dy && dx > dz) {
                return vector.getX() > 8 ? Direction.EAST : Direction.WEST;
            }
            return vector.getY() > 8 ? Direction.UP : Direction.DOWN;
        }

        public Direction getHorizontalFacing() {
            int dx = Math.abs(vector.getX() - 8);
            int dz = Math.abs(vector.getZ() - 8);
            if (dz > dx) {
                return vector.getZ() > 8 ? Direction.SOUTH : Direction.NORTH;
            }
            return vector.getX() > 8 ? Direction.EAST : Direction.WEST;
        }

        /**
         * Whether the part is on the top half of the block.
         */
        public boolean isTop() {
            return vector.getY() > 8;
        }

        /**
         * Whether the part is on the right half of the block when facing the specified direction.
         */
        public boolean isRight(Direction facing) {
            return switch (facing) {
                case SOUTH -> vector.getX() < 8;
                case NORTH -> vector.getX() > 8;
                case EAST -> vector.getZ() > 8;
                case WEST -> vector.getZ() < 8;
                default -> false;
            };
        }
    }
}
