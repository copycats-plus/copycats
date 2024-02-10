package com.copycatsplus.copycats.mixin.compat;


import com.copycatsplus.copycats.Copycats;
import com.firemerald.additionalplacements.common.CommonModEventHandler;
import com.firemerald.additionalplacements.common.ConfigCommon;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CommonModEventHandler.class, priority = 1100)
public class AdditionalPlacementsCommonEventHandlerMixin {

    @Redirect(method = "tryAdd", at = @At(value = "INVOKE", target = "Lcom/firemerald/additionalplacements/common/ConfigCommon;isValidForGeneration(Lnet/minecraft/resources/ResourceLocation;)Z"), remap = false, require = 0, expect = 0)
    private static boolean copycats$disableModCompat(ConfigCommon instance, ResourceLocation blockName) {
        if (blockName.getNamespace().equalsIgnoreCase(Copycats.MODID)) {
            return false;
        } else {
           return instance.isValidForGeneration(blockName);
        }
    }
}
