package com.copycatsplus.copycats.fabric.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeometryHelper.class)
@Pseudo
public class GeometryHelperMixin {
    @Inject(
            method = "longestAxis(FFF)Lnet/minecraft/core/Direction$Axis;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void longestAxis(float normalX, float normalY, float normalZ, CallbackInfoReturnable<Direction.Axis> cir) {
        cir.setReturnValue(MutableQuad.longestAxis(normalX, normalY, normalZ));
    }
}
