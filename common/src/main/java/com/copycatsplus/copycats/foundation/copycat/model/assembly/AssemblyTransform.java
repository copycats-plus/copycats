package com.copycatsplus.copycats.foundation.copycat.model.assembly;

/**
 * Transforms model assembly operations which implement {@link AssemblyTransform.Transformable}.
 * <p>
 * This is intended to be implemented by consumers of the {@link CopycatRenderContext} API to apply transformations to the assembly process.
 */
@FunctionalInterface
public interface AssemblyTransform {
    /**
     * The identity transform which does nothing.
     */
    AssemblyTransform IDENTITY = t -> {
    };

    /**
     * Apply the transform to the given {@link Transformable}.
     *
     * @param t The transformable to apply the transform to.
     */
    void apply(Transformable<?> t);

    default AssemblyTransform andThen(AssemblyTransform after) {
        return t -> {
            apply(t);
            after.apply(t);
        };
    }

    interface Transformable<Self extends Transformable<Self>> {
        /**
         * Rotate in 90 degree increments around the X axis clockwise, centered around the center of the model.
         *
         * @param angle The angle to rotate by, in degrees.
         */
        Self rotateX(int angle);

        /**
         * Rotate in 90 degree increments around the Y axis clockwise, centered around the center of the model.
         *
         * @param angle The angle to rotate by, in degrees.
         */
        Self rotateY(int angle);

        /**
         * Rotate in 90 degree increments around the Z axis clockwise, centered around the center of the model.
         *
         * @param angle The angle to rotate by, in degrees.
         */
        Self rotateZ(int angle);

        /**
         * Flip the model along the X axis, centered around the center of the model.
         *
         * @param flip Whether to flip the model. If false, this method should be a no-op.
         */
        Self flipX(boolean flip);

        /**
         * Flip the model along the Y axis, centered around the center of the model.
         *
         * @param flip Whether to flip the model. If false, this method should be a no-op.
         */
        Self flipY(boolean flip);

        /**
         * Flip the model along the Z axis, centered around the center of the model.
         *
         * @param flip Whether to flip the model. If false, this method should be a no-op.
         */
        Self flipZ(boolean flip);
    }
}
