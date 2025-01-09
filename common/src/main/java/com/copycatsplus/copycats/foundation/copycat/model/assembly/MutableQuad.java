package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A mutable container for quad data.
 */
public class MutableQuad implements AssemblyTransform.Transformable<MutableQuad> {
    public List<MutableVertex> vertices;
    @Nullable
    public Direction cullFace;
    public boolean disableFinalAutoCull = false;
    List<Mutation> mutations = new ArrayList<>();

    public MutableQuad(List<MutableVertex> vertices, @Nullable Direction cullFace) {
        this.vertices = vertices;
        this.cullFace = cullFace;
    }

    public Direction computeLightFace() {
        final MutableVec3 normal = computeFaceNormal();
        return switch (longestAxis(normal.x, normal.y, normal.z)) {
            case X -> normal.x() > 0 ? Direction.EAST : Direction.WEST;
            case Y -> normal.y() > 0 ? Direction.UP : Direction.DOWN;
            case Z -> normal.z() > 0 ? Direction.SOUTH : Direction.NORTH;
        };
    }

    private static final double EPSILON = 0.05;

    public static Axis longestAxis(double normalX, double normalY, double normalZ) {
        Axis result = Axis.Y;
        double longest = Math.abs(normalY);
        double a = Math.abs(normalX);

        if (a > longest + EPSILON) {
            result = Axis.X;
            longest = a;
        }

        return Math.abs(normalZ) > longest + EPSILON
                ? Axis.Z : result;
    }

    public MutableVec3 computeFaceNormal() {
        final double x0 = vertices.get(0).xyz.x;
        final double y0 = vertices.get(0).xyz.y;
        final double z0 = vertices.get(0).xyz.z;
        final double x1 = vertices.get(1).xyz.x;
        final double y1 = vertices.get(1).xyz.y;
        final double z1 = vertices.get(1).xyz.z;
        final double x2 = vertices.get(2).xyz.x;
        final double y2 = vertices.get(2).xyz.y;
        final double z2 = vertices.get(2).xyz.z;
        final double x3 = vertices.get(3).xyz.x;
        final double y3 = vertices.get(3).xyz.y;
        final double z3 = vertices.get(3).xyz.z;

        final double dx0 = x2 - x0;
        final double dy0 = y2 - y0;
        final double dz0 = z2 - z0;
        final double dx1 = x3 - x1;
        final double dy1 = y3 - y1;
        final double dz1 = z3 - z1;

        double normX = dy0 * dz1 - dz0 * dy1;
        double normY = dz0 * dx1 - dx0 * dz1;
        double normZ = dx0 * dy1 - dy0 * dx1;

        double l = (float) Math.sqrt(normX * normX + normY * normY + normZ * normZ);

        if (l != 0) {
            normX /= l;
            normY /= l;
            normZ /= l;
        }

        return new MutableVec3(normX, normY, normZ);
    }

    public MutableQuad mutate() {
        for (Mutation mutation : mutations) {
            for (MutableVertex vertex : vertices) {
                mutation.mutate(vertex.xyz);
            }
            if (mutation.type() == Mutation.MutationType.MIRROR) {
                reverseWinding();
            }
            cullFace = mutation.mutate(cullFace);
        }
        return this;
    }

    public MutableQuad undoMutate() {
        for (int i = mutations.size() - 1; i >= 0; i--) {
            Mutation mutation = mutations.get(i);
            for (MutableVertex vertex : vertices) {
                mutation.undoMutate(vertex.xyz);
            }
            if (mutation.type() == Mutation.MutationType.MIRROR) {
                reverseWinding();
            }
            cullFace = mutation.undoMutate(cullFace);
        }
        return this;
    }

    private void reverseWinding() {
        MutableVertex temp = vertices.get(0);
        vertices.set(0, vertices.get(1));
        vertices.set(1, temp);
        temp = vertices.get(2);
        vertices.set(2, vertices.get(3));
        vertices.set(3, temp);
    }

    @Override
    public MutableQuad rotateX(int angle) {
        mutations.add(new Mutation(Mutation.MutationType.ROTATE_X, angle));
        return this;
    }

    @Override
    public MutableQuad rotateY(int angle) {
        mutations.add(new Mutation(Mutation.MutationType.ROTATE_Y, angle));
        return this;
    }

    @Override
    public MutableQuad rotateZ(int angle) {
        mutations.add(new Mutation(Mutation.MutationType.ROTATE_Z, angle));
        return this;
    }

    @Override
    public MutableQuad flipX(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(Mutation.MutationType.MIRROR, 0));
        return this;
    }

    @Override
    public MutableQuad flipY(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(Mutation.MutationType.MIRROR, 1));
        return this;
    }

    @Override
    public MutableQuad flipZ(boolean flip) {
        if (!flip) return this;
        mutations.add(new Mutation(Mutation.MutationType.MIRROR, 2));
        return this;
    }
}
