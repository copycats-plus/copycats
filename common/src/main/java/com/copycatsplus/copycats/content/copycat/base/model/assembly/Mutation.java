package com.copycatsplus.copycats.content.copycat.base.model.assembly;


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

    public enum MutationType {
        ROTATE_X,
        ROTATE_Y,
        ROTATE_Z,
        MIRROR
    }
}
