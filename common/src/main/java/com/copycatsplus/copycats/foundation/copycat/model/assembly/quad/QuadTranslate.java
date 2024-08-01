package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Translates a quad by a vector in block space.
 *
 * @param x The amount to translate along the x-axis.
 * @param y The amount to translate along the y-axis.
 * @param z The amount to translate along the z-axis.
 */
public record QuadTranslate(double x, double y, double z) implements QuadTransform {

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            quad.vertices.get(i).xyz.add(this.x, this.y, this.z);
        }
        return true;
    }
}
