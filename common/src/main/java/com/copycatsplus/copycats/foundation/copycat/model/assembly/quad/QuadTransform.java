package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Transforms a quad by mutating its vertices or other properties.
 */
public interface QuadTransform {
    /**
     * Transforms the quad by mutating its vertices or other properties.
     * <p>
     * Return false to discard the quad.
     */
    boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite);
}
