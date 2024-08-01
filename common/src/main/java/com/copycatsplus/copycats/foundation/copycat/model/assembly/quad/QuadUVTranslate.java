package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedU;
import static com.simibubi.create.foundation.block.render.SpriteShiftEntry.getUnInterpolatedV;

/**
 * Translate the UV coordinates of a quad.
 *
 * @param face    The face to translate the UV coordinates of.
 * @param offsetU The U coordinate offset in voxel space.
 * @param offsetV The V coordinate offset in voxel space.
 */
public record QuadUVTranslate(Direction face, float offsetU, float offsetV) implements QuadTransform {

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        if (quad.computeLightFace() == face) {
            for (int vertex = 0; vertex < 4; vertex++) {
                float u = getUnInterpolatedU(sprite, quad.vertices.get(vertex).uv.u) + offsetU;
                float v = getUnInterpolatedV(sprite, quad.vertices.get(vertex).uv.v) + offsetV;

                quad.vertices.get(vertex).uv.u = sprite.getU(u);
                quad.vertices.get(vertex).uv.v = sprite.getV(v);
            }
        }
        return true;
    }
}
