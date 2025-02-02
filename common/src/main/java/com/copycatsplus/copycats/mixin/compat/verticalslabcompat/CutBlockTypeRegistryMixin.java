package com.copycatsplus.copycats.mixin.compat.verticalslabcompat;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import net.mehvahdjukaar.moonlight.api.set.BlockTypeRegistry;
import net.mehvahdjukaar.vsc.CutBlockType;
import net.mehvahdjukaar.vsc.CutBlockTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@ModMixin(requiredMods = Mods.VERTICAL_SLAB_COMPAT)
@Mixin(value = CutBlockTypeRegistry.class, remap = false)
public class CutBlockTypeRegistryMixin {

    @Inject(method = "detectTypeFromBlock", at = @At("HEAD"), cancellable = true)
    private void copycats$disableVerticalCompat(Block block, ResourceLocation baseRes, CallbackInfoReturnable<Optional<CutBlockType>> cir) {
        if (baseRes.getNamespace().equals(Copycats.MODID)) cir.setReturnValue(Optional.empty());
    }
}
