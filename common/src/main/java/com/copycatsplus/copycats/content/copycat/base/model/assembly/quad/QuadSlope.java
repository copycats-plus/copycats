package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public record QuadSlope(Direction face, QuadSlopeFunction func) implements QuadTransform {

    private static final double EPSILON = 0.02 / 16;

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            MutableVec3 vertex = quad.vertices.get(i).xyz;

            double a;
            double b;

            switch (this.face.getAxis()) {
                case X:
                    a = vertex.y;
                    b = vertex.z;
                    break;
                case Y:
                    a = vertex.x;
                    b = vertex.z;
                    break;
                case Z:
                    a = vertex.x;
                    b = vertex.y;
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }

            double output = this.func.apply(a, b);
            // prevent texture UVs from messing up due to loss of precision
            if (Math.abs(output) < EPSILON) {
                output = EPSILON;
            }

            switch (this.face.getAxis()) {
                case X:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.x *= output;
                    } else {
                        vertex.x = 1 - output * (1 - vertex.x);
                    }
                    break;
                case Y:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.y *= output;
                    } else {
                        vertex.y = 1 - output * (1 - vertex.y);
                    }
                    break;
                case Z:
                    if (this.face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        vertex.z *= output;
                    } else {
                        vertex.z = 1 - output * (1 - vertex.z);
                    }
                    break;
                default:
                    throw new RuntimeException("Unexpected value: " + this.face.getAxis());
            }
        }
    }

    public static double map(double fromStart, double fromEnd, double toStart, double toEnd, double value) {
        return toStart + (value - fromStart) / (fromEnd - fromStart) * (toEnd - toStart);
    }

    @FunctionalInterface
    public interface QuadSlopeFunction {
        /**
         * Given the two coordinates of a vertex, return a scaling factor for the height in the remaining axis.
         * <ul>
         * <li>If the X axis is being mapped, the first parameter is the Y coordinate and the second parameter is the Z coordinate.</li>
         * <li>If the Y axis is being mapped, the first parameter is the X coordinate and the second parameter is the Z coordinate.</li>
         * <li>If the Z axis is being mapped, the first parameter is the X coordinate and the second parameter is the Y coordinate.</li>
         * </ul>
         *
         * @param a The first coordinate
         * @param b The second coordinate
         * @return The scaling factor for the height in the remaining axis
         */
        double apply(double a, double b);
    }
}
