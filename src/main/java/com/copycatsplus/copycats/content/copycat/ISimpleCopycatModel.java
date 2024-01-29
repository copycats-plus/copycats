package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface ISimpleCopycatModel {

    /**
     * Assemble the quads of a piece of copycat material.
     *
     * @param sourceQuads The source model to copy from.
     * @param destQuads   The destination model to copy to.
     * @param rotation    Number of degrees to rotate the whole operation for. Only supports multiples of 90. A value of 0 corresponds to a model facing south.
     * @param flipY       Whether to flip the whole operation vertically.
     * @param offset      In voxel space, the final position of the assembled piece.
     * @param select      In voxel space, the selection on the source model to copy from.
     * @param cull        Faces to skip rendering in the destination model. Changed automatically according to `rotation` and `flipY`.
     */
    default void assemblePiece(List<BakedQuad> sourceQuads, List<BakedQuad> destQuads, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        select.rotate(rotation).flipY(flipY);
        offset.rotate(rotation).flipY(flipY);
        cull.rotate(rotation).flipY(flipY);
        for (BakedQuad quad : sourceQuads) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            destQuads.add(BakedQuadHelper.cloneWithCustomGeometry(quad,
                    BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f))));
        }
    }

    default MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    default MutableVec3 vec3(float x, float y, float z) {
        return new MutableVec3(x, y, z);
    }

    default MutableAABB aabb(float sizeX, float sizeY, float sizeZ) {
        return new MutableAABB(sizeX, sizeY, sizeZ);
    }

    class MutableCullFace {

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

        private MutableCullFace(int mask) {
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

        public MutableCullFace flipY(boolean flip) {
            if (!flip) return this;
            return set(down, up, north, south, east, west);
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

    class MutableVec3 {
        public float x;
        public float y;
        public float z;

        private MutableVec3(float x, float y, float z) {
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

        public MutableVec3 flipY(boolean flip) {
            if (!flip) return this;
            return set(x, 16 - y, z);
        }

        public Vec3 toVec3() {
            return new Vec3(x / 16f, y / 16f, z / 16f);
        }

        public MutableVec3 set(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }
    }

    class MutableAABB {
        public float minX;
        public float minY;
        public float minZ;
        public float maxX;
        public float maxY;
        public float maxZ;

        private MutableAABB(float sizeX, float sizeY, float sizeZ) {
            set(0, 0, 0, sizeX, sizeY, sizeZ);
        }

        public MutableAABB move(float dX, float dY, float dZ) {
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

        public MutableAABB flipY(boolean flip) {
            if (!flip) return this;
            return set(minX, 16 - minY, minZ, maxX, 16 - maxY, maxZ);
        }

        public AABB toAABB() {
            return new AABB(minX / 16f, minY / 16f, minZ / 16f, maxX / 16f, maxY / 16f, maxZ / 16f);
        }

        public MutableAABB set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
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
