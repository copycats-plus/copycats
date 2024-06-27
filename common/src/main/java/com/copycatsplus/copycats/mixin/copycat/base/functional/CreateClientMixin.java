package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.CopycatsClient;
import com.simibubi.create.CreateClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CreateClient.class, remap = false)
public class CreateClientMixin {
    @Inject(method = "invalidateRenderers", at = @At("HEAD"))
    private static void invalidateRenderers(CallbackInfo ci) {
        CopycatsClient.BUFFER_CACHE.invalidate();
    }
}
