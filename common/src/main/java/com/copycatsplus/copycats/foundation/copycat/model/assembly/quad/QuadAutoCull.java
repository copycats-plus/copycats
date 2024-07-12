package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

/**
 * Automatically assign cull face according to quad position and orientation.
 */
public final class QuadAutoCull implements QuadTransform {

    public static QuadAutoCull INSTANCE = new QuadAutoCull();

    private static final double EPSILON = 0.02 / 16;

    private QuadAutoCull() {
    }

    @Override
    public void transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        Direction lightFace = quad.computeLightFace();
        double target = lightFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : 0;
        Axis axis = lightFace.getAxis();
        for (MutableVertex vertex : quad.vertices) {
            if (Math.abs(vertex.xyz.get(axis) - target) > EPSILON) {
                quad.cullFace = null;
                quad.disableFinalAutoCull = true;
                return;
            }
        }
        quad.cullFace = lightFace;
        quad.disableFinalAutoCull = true;
    }
}
