package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;


public interface QuadTransform {
    void transformVertices(MutableQuad quad, TextureAtlasSprite sprite);
}
