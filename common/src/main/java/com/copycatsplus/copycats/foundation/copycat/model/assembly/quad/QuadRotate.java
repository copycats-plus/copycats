package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

/**
 * Freely rotate the quad around the pivot point. Rotations are performed in the order of X, Y, Z.
 * <p>
 * Rotations of any angle and of multiple axes are allowed.
 *
 * @param pivot    The pivot point to rotate around, in block space.
 * @param rotation The rotation to apply, in axis-angle representation (degrees).
 */
public record QuadRotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rotation) implements QuadTransform {

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            quad.vertices.get(i).xyz.subtract(this.pivot).rotate(this.rotation).add(this.pivot);
        }
        return true;
    }
}
