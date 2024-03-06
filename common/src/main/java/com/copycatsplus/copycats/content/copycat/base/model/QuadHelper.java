package com.copycatsplus.copycats.content.copycat.base.model;

import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuadHelper {

    /**
     * Assemble the quads of a piece of copycat material.
     *
     * @param context  Source and destination quads.
     * @param rotation Number of degrees to rotate the whole operation for. Only supports multiples of 90. A value of 0 corresponds to a model facing south.
     * @param flipY    Whether to flip the whole operation vertically.
     * @param offset   In voxel space, the final position of the assembled piece.
     * @param select   In voxel space, the selection on the source model to copy from.
     * @param cull     Faces to skip rendering in the destination model. Changed automatically according to `rotation` and `flipY`.
     */
    @ExpectPlatform
    public static <Source, Destination> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {

    }

    /**
     * Assemble the quads of a piece of copycat material.
     *
     * @param context    Source and destination quads.
     * @param rotation   Number of degrees to rotate the whole operation for. Only supports multiples of 90. A value of 0 corresponds to a model facing south.
     * @param flipY      Whether to flip the whole operation vertically.
     * @param offset     In voxel space, the final position of the assembled piece.
     * @param select     In voxel space, the selection on the source model to copy from.
     * @param cull       Faces to skip rendering in the destination model. Changed automatically according to `rotation` and `flipY`.
     * @param transforms Additional transforms to apply to the quads.
     */
    @ExpectPlatform
    public static <Source, Destination> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {

    }

    /**
     * Copy ALL quads from source to destination without modification.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(CopycatRenderContext<Source, Destination> context) {

    }

    /**
     * Copy a quad from source to destination without modification.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source source, Destination destination) {

    }

    /**
     * Copy ALL quads from source to destination while applying the specified crop and move.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move) {

    }

    /**
     * Copy ALL quads from source to destination while applying the specified transforms.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move, QuadTransform... transforms) {

    }

    /**
     * Copy a quad from source to destination while applying the specified crop and move.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {

    }

    /**
     * Copy a quad from source to destination while applying the specified transforms.
     */
    @ExpectPlatform
    public static <Source, Destination> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {

    }

    public record CopycatRenderContext<Source, Destination>(Source source, Destination destination) {

    }

    public interface QuadTransform {
        int[] transformVertices(int[] vertexData, TextureAtlasSprite sprite);

        QuadTransform rotate(int angle);

        QuadTransform flipX(boolean flip);

        QuadTransform flipY(boolean flip);

        QuadTransform flipZ(boolean flip);
    }

    public enum MutationType {
        ROTATE,
        MIRROR
    }

    public record Mutation(MutationType type, int value) {
        public MutableVec3 mutate(MutableVec3 vec3) {
            return switch (type) {
                case ROTATE -> vec3.rotate(value);
                case MIRROR -> {
                    if (value == 0) yield vec3.flipX(true);
                    else if (value == 1) yield vec3.flipY(true);
                    else if (value == 2) yield vec3.flipZ(true);
                    else yield vec3;
                }
            };
        }

        public MutableVec3 undoMutate(MutableVec3 vec3) {
            return switch (type) {
                case ROTATE -> vec3.rotate(-value);
                case MIRROR -> {
                    if (value == 0) yield vec3.flipX(true);
                    else if (value == 1) yield vec3.flipY(true);
                    else if (value == 2) yield vec3.flipZ(true);
                    else yield vec3;
                }
            };
        }
    }

    public static class MutableRotation implements QuadTransform {
        private final Vec3 pivot;
        private final Vec3 rotation;
        List<Mutation> mutations = new ArrayList<>(2);

        public MutableRotation(MutableVec3 pivot, MutableVec3 rotation) {
            this.pivot = pivot.toVec3Unscaled();
            this.rotation = rotation.toVec3Unscaled();
        }

        @Override
        public int[] transformVertices(int[] vertexData, TextureAtlasSprite sprite) {
            vertexData = Arrays.copyOf(vertexData, vertexData.length);
            MutableVec3 mutableVertex = new MutableVec3(0, 0, 0);
            for (int i = 0; i < 4; i++) {
                Vec3 vertex = BakedQuadHelper.getXYZ(vertexData, i);
                undoMutate(mutableVertex.set(vertex.x * 16, vertex.y * 16, vertex.z * 16));
                Vec3 rotated = VecHelper.rotate(mutableVertex.toVec3Unscaled().subtract(pivot), rotation).add(pivot);
                BakedQuadHelper.setXYZ(vertexData, i, mutate(mutableVertex.set(rotated.x, rotated.y, rotated.z)).toVec3());
            }
            return vertexData;
        }

        private MutableVec3 mutate(MutableVec3 vec3) {
            for (Mutation mutation : mutations) {
                mutation.mutate(vec3);
            }
            return vec3;
        }

        private MutableVec3 undoMutate(MutableVec3 vec3) {
            for (int i = mutations.size() - 1; i >= 0; i--) {
                Mutation mutation = mutations.get(i);
                mutation.undoMutate(vec3);
            }
            return vec3;
        }

        @Override
        public QuadTransform rotate(int angle) {
            mutations.add(new Mutation(MutationType.ROTATE, angle));
            return this;
        }

        @Override
        public QuadTransform flipX(boolean flip) {
            if (!flip) return this;
            mutations.add(new Mutation(MutationType.MIRROR, 0));
            return this;
        }

        @Override
        public QuadTransform flipY(boolean flip) {
            if (!flip) return this;
            mutations.add(new Mutation(MutationType.MIRROR, 1));
            return this;
        }

        @Override
        public QuadTransform flipZ(boolean flip) {
            if (!flip) return this;
            mutations.add(new Mutation(MutationType.MIRROR, 2));
            return this;
        }
    }

    public static class MutableCullFace {

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

        MutableCullFace(int mask) {
            set((mask & UP) > 0, (mask & DOWN) > 0, (mask & NORTH) > 0, (mask & SOUTH) > 0, (mask & EAST) > 0, (mask & WEST) > 0);
        }

        public MutableCullFace rotate(int angle) {
            angle = angle % 360;
            if (angle < 0) angle += 360;
            return switch (angle) {
                case 90 -> set(up, down, west, east, north, south);
                case 180 -> set(up, down, south, north, west, east);
                case 270 -> set(up, down, east, west, south, north);
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

    public static class MutableVec3 {
        public double x;
        public double y;
        public double z;

        MutableVec3(double x, double y, double z) {
            set(x, y, z);
        }

        public MutableVec3 rotate(int angle) {
            angle = angle % 360;
            if (angle < 0) angle += 360;
            return switch (angle) {
                case 90 -> set(16 - z, y, x);
                case 180 -> set(16 - x, y, 16 - z);
                case 270 -> set(z, y, 16 - x);
                default -> this;
            };
        }

        public MutableVec3 flipX(boolean flip) {
            if (!flip) return this;
            return set(16 - x, y, z);
        }

        public MutableVec3 flipY(boolean flip) {
            if (!flip) return this;
            return set(x, 16 - y, z);
        }

        public MutableVec3 flipZ(boolean flip) {
            if (!flip) return this;
            return set(x, y, 16 - z);
        }

        public Vec3 toVec3() {
            return new Vec3(x / 16f, y / 16f, z / 16f);
        }

        public Vec3 toVec3Unscaled() {
            return new Vec3(x, y, z);
        }

        public MutableVec3 set(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }
    }

    public static class MutableAABB {
        public double minX;
        public double minY;
        public double minZ;
        public double maxX;
        public double maxY;
        public double maxZ;

        MutableAABB(double sizeX, double sizeY, double sizeZ) {
            set(0, 0, 0, sizeX, sizeY, sizeZ);
        }

        public MutableAABB move(double dX, double dY, double dZ) {
            minX += dX;
            maxX += dX;
            minY += dY;
            maxY += dY;
            minZ += dZ;
            maxZ += dZ;
            return this;
        }

        public MutableAABB rotate(int angle) {
            angle = angle % 360;
            if (angle < 0) angle += 360;
            return switch (angle) {
                case 90 -> set(16 - minZ, minY, minX, 16 - maxZ, maxY, maxX);
                case 180 -> set(16 - minX, minY, 16 - minZ, 16 - maxX, maxY, 16 - maxZ);
                case 270 -> set(minZ, minY, 16 - minX, maxZ, maxY, 16 - maxX);
                default -> this;
            };
        }

        public MutableAABB flipX(boolean flip) {
            if (!flip) return this;
            return set(16 - minX, minY, minZ, 16 - maxX, maxY, maxZ);
        }

        public MutableAABB flipY(boolean flip) {
            if (!flip) return this;
            return set(minX, 16 - minY, minZ, maxX, 16 - maxY, maxZ);
        }

        public MutableAABB flipZ(boolean flip) {
            if (!flip) return this;
            return set(minX, minY, 16 - minZ, maxX, maxY, 16 - maxZ);
        }

        public AABB toAABB() {
            return new AABB(minX / 16f, minY / 16f, minZ / 16f, maxX / 16f, maxY / 16f, maxZ / 16f);
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
}
