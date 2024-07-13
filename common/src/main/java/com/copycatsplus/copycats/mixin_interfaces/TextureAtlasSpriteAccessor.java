package com.copycatsplus.copycats.mixin_interfaces;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface TextureAtlasSpriteAccessor {

    TextureAtlasSprite.Info copycats$info();

    int copycats$mipmap();

    NativeImage copycats$nativeImage();
}