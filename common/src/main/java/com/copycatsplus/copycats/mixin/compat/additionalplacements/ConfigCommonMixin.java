package com.copycatsplus.copycats.mixin.compat.additionalplacements;

import com.copycatsplus.copycats.Copycats;
import com.firemerald.additionalplacements.common.ConfigCommon;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevent AdditionalPlacements from adding extra states to our mod blocks.
 * <p>
 * Will appear to error but works and builds fine.
 */
@Pseudo
@Mixin(value = {ConfigCommon.class})
public class ConfigCommonMixin {

    @Inject(
            at = @At("HEAD"),
            method = "isValidForGeneration(Lnet/minecraft/resources/ResourceLocation;)Z",
            cancellable = true,
            require = 0
    )
    private void copycats$disableModCompat(ResourceLocation blockName, CallbackInfoReturnable<Boolean> cir) {
        if (blockName.getNamespace().equalsIgnoreCase(Copycats.MODID)) {
            cir.setReturnValue(false);
        }
    }
}
