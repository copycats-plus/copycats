package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadRotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rotation) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            quad.vertices.get(i).xyz.subtract(this.pivot).rotate(this.rotation).add(this.pivot);
        }
    }
}
