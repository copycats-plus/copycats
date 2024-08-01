package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableAABB;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

/**
 * Automatically assign cull face according to quad position and orientation.
 *
 * @param cullingBox The bounding box to cull against. Faces that are touching the box are allowed to be culled.
 */
public record QuadAutoCull(MutableAABB cullingBox) implements QuadTransform {

    public static QuadAutoCull BLOCK = new QuadAutoCull(new MutableAABB(1, 1, 1));

    private static final double EPSILON = 0.02 / 16;

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        Direction lightFace = quad.computeLightFace();
        Axis axis = lightFace.getAxis();
        double target = lightFace.getAxisDirection() == Direction.AxisDirection.POSITIVE
                ? cullingBox.getMax(axis)
                : cullingBox.getMin(axis);
        for (MutableVertex vertex : quad.vertices) {
            if (Math.abs(vertex.xyz.get(axis) - target) > EPSILON) {
                quad.cullFace = null;
                quad.disableFinalAutoCull = true;
                return true;
            }
        }
        quad.cullFace = lightFace;
        quad.disableFinalAutoCull = true;
        return true;
    }
}
