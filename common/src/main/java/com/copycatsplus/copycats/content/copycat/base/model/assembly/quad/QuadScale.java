package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadScale(MutableVec3.AsPivot pivot, MutableVec3.AsScale scale) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            quad.vertices.get(i).xyz.subtract(this.pivot).multiply(this.scale).add(this.pivot);
        }
    }
}
