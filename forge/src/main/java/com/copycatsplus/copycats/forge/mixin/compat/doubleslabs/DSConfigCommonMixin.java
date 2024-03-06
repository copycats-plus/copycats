package com.copycatsplus.copycats.forge.mixin.compat.doubleslabs;

import cjminecraft.doubleslabs.common.config.DSConfig;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DSConfig.Common.class)
@Pseudo
public class DSConfigCommonMixin {

    @Inject(method = "isBlacklistedHorizontalSlab", at = @At("HEAD"), cancellable = true, remap = false)
    private void copycats$alwaysBlacklistHorizontal(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof CopycatBlock) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isBlacklistedVerticalSlab", at = @At("HEAD"), cancellable = true, remap = false)
    private void copycats$alwaysBlacklistVertical(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof CopycatBlock) {
            cir.setReturnValue(false);
        }
    }
}
