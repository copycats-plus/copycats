package com.copycatsplus.copycats.content.copycat.base.multistate;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MultiStateTextureAtlasSprite extends TextureAtlasSprite {
    private final String property;

    public MultiStateTextureAtlasSprite(String property, TextureAtlasSprite wrapped) {
        super(wrapped.atlasLocation(), wrapped.contents(), (int) (wrapped.getX() / wrapped.getU0()), (int) (wrapped.getY() / wrapped.getV0()), wrapped.getX(), wrapped.getY());
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
