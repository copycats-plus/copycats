package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record QuadTranslate(double x, double y, double z) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            quad.vertices.get(i).xyz.add(this.x, this.y, this.z);
        }
    }
}
