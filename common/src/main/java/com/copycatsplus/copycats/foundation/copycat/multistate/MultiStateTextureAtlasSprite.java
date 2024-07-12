package com.copycatsplus.copycats.foundation.copycat.multistate;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.ApiStatus;

/**
 * A hack to pass the currently rendering property to the block color provider.
 */
@ApiStatus.Internal
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
