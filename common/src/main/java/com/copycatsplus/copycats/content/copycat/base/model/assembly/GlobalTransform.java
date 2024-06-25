package com.copycatsplus.copycats.content.copycat.base.model.assembly;

@FunctionalInterface
public interface GlobalTransform {
    public static final GlobalTransform IDENTITY = t -> {
    };

    void apply(Transformable<?> t);

    public interface Transformable<Self extends Transformable<Self>> {
        /**
         * Rotate in 90 degree increments around the X axis clockwise
         */
        Self rotateX(int angle);

        /**
         * Rotate in 90 degree increments around the Y axis clockwise
         */
        Self rotateY(int angle);

        /**
         * Rotate in 90 degree increments around the Z axis clockwise
         */
        Self rotateZ(int angle);

        Self flipX(boolean flip);

        Self flipY(boolean flip);

        Self flipZ(boolean flip);
    }
}
