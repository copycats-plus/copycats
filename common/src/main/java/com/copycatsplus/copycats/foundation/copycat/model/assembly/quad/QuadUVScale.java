package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedU;
import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedV;

/**
 * Scale the UV coordinates of a quad around a pivot point.
 *
 * @param face   The face to scale the UV coordinates of.
 * @param pivotU The U coordinate of the pivot of scaling, in voxel space.
 * @param pivotV The V coordinate of the pivot of scaling, in voxel space.
 * @param scaleU The U coordinate scale factor. A value greater than 1 causes the texture to look smaller.
 * @param scaleV The V coordinate scale factor. A value greater than 1 causes the texture to look smaller.
 */
public record QuadUVScale(Direction face,
                          float pivotU, float pivotV,
                          float scaleU, float scaleV) implements QuadTransform {

    @Override
    public void transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        if (quad.computeLightFace() == face) {
            for (int vertex = 0; vertex < 4; vertex++) {
                float u = getUnInterpolatedU(sprite, quad.vertices.get(vertex).uv.u) - pivotU;
                float v = getUnInterpolatedV(sprite, quad.vertices.get(vertex).uv.v) - pivotV;

                quad.vertices.get(vertex).uv.u = sprite.getU(u * scaleU + pivotU);
                quad.vertices.get(vertex).uv.v = sprite.getV(v * scaleV + pivotV);
            }
        }
    }
}
