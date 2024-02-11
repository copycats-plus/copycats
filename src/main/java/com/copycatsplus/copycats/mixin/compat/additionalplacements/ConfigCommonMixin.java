package com.copycatsplus.copycats.mixin.compat.additionalplacements;

import com.copycatsplus.copycats.Copycats;
import com.firemerald.additionalplacements.common.ConfigCommon;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(value = {ConfigCommon.class})
public class ConfigCommonMixin {

    //Will appear to error but works and builds fine.
    //Forces our mod to never get extra states
    @Inject(
            at = @At("HEAD"),
            method = "isValidForGeneration(Lnet/minecraft/resources/ResourceLocation;)Z",
            cancellable = true
    )
    private void copycats$disableModCompat(ResourceLocation blockName, CallbackInfoReturnable<Boolean> cir) {
        if (blockName.getNamespace().equalsIgnoreCase(Copycats.MODID)) {
            cir.setReturnValue(false);
        }
    }
}
