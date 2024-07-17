package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedU;
import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedV;

/**
 * Rotate the UV coordinates of a quad around a pivot point.
 * <p>
 * Rotations of any angle is allowed.
 *
 * @param face     The face to rotate the UV coordinates of.
 * @param pivotU   The U coordinate of the pivot of rotation, in voxel space.
 * @param pivotV   The V coordinate of the pivot of rotation, in voxel space.
 * @param rotation The rotation to apply, in degrees.
 */
public record QuadUVRotate(Direction face, float pivotU, float pivotV, float rotation) implements QuadTransform {

    @Override
    public void transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        if (quad.computeLightFace() == face) {
            for (int vertex = 0; vertex < 4; vertex++) {
                float u = getUnInterpolatedU(sprite, quad.vertices.get(vertex).uv.u) - pivotU;
                float v = getUnInterpolatedV(sprite, quad.vertices.get(vertex).uv.v) - pivotV;

                double cos = Math.cos(Math.toRadians(rotation));
                double sin = Math.sin(Math.toRadians(rotation));

                quad.vertices.get(vertex).uv.u = sprite.getU(u * cos - v * sin + pivotU);
                quad.vertices.get(vertex).uv.v = sprite.getV(u * sin + v * cos + pivotV);
            }
        }
    }
}
