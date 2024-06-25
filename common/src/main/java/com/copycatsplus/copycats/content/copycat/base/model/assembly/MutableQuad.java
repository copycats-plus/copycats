package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import java.util.ArrayList;
import java.util.List;

public class MutableQuad implements GlobalTransform.Transformable<MutableQuad> {
    public List<MutableVertex> vertices;
    List<Mutation> mutations = new ArrayList<>();

    public MutableQuad(List<MutableVertex> vertices) {
        this.vertices = vertices;
    }

    public MutableQuad mutate() {
        for (Mutation mutation : mutations) {
            for (MutableVertex vertex : vertices) {
                mutation.mutate(vertex.xyz);
            }
        }
        return this;
    }

    public MutableQuad undoMutate() {
        for (int i = mutations.size() - 1; i >= 0; i--) {
            Mutation mutation = mutations.get(i);
            for (MutableVertex vertex : vertices) {
                mutation.undoMutate(vertex.xyz);
            }
        }
        return this;
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
