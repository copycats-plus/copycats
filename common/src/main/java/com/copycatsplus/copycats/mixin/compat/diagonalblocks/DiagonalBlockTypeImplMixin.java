package com.copycatsplus.copycats.mixin.compat.diagonalblocks;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import fuzs.diagonalblocks.api.v2.impl.DiagonalBlockTypeImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@ModMixin(requiredMods = {Mods.DIAGONAL_WALLS, Mods.DIAGONAL_FENCES, Mods.DIAGONAL_WINDOWS})
@Mixin(DiagonalBlockTypeImpl.class)
public class DiagonalBlockTypeImplMixin {

    @Inject(method = "isTarget", at = @At("HEAD"), cancellable = true)
    private void copycats$disableDiagonalCopycats(ResourceLocation resourceLocation, Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof ICopycatBlock) cir.setReturnValue(false);
    }
}
