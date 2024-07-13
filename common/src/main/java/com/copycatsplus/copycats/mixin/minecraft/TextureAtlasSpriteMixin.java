package com.copycatsplus.copycats.mixin.minecraft;

import com.copycatsplus.copycats.mixin_interfaces.TextureAtlasSpriteAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlasSprite.class)
public class TextureAtlasSpriteMixin implements TextureAtlasSpriteAccessor {

    @Unique
    private TextureAtlasSprite.Info copycats$info;
    @Unique
    private int copycats$mipmap;
    @Unique
    private NativeImage copycats$nativeImage;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void copycats$assignObjects(TextureAtlas atlas, TextureAtlasSprite.Info spriteInfo, int mipLevel, int storageX, int storageY, int x, int y, NativeImage image, CallbackInfo ci) {
        this.copycats$info = spriteInfo;
        this.copycats$mipmap = mipLevel;
        this.copycats$nativeImage = image;
    }

    @Override
    public net.minecraft.client.renderer.texture.TextureAtlasSprite.Info copycats$info() {
        return copycats$info;
    }

    @Override
    public int copycats$mipmap() {
        return copycats$mipmap;
    }

    @Override
    public NativeImage copycats$nativeImage() {
        return copycats$nativeImage;
    }
}
