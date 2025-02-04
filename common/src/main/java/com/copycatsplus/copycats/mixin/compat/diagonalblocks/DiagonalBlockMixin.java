package com.copycatsplus.copycats.mixin.compat.diagonalblocks;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes sure that copycat fences/panes are not processed by Diagonal Fences/Windows
 * <p>
 * This patch has to be applied after Diagonal Fences modifies the FenceBlock class
 */
@ModMixin(requiredMods = {Mods.DIAGONAL_FENCES, Mods.DIAGONAL_WINDOWS}) // diagonal walls is not available in 1.19
@Mixin(value = {FenceBlock.class, IronBarsBlock.class}, priority = 1100)
public abstract class DiagonalBlockMixin extends CrossCollisionBlock {

    public DiagonalBlockMixin(float pNodeWidth, float pExtensionWidth, float pNodeHeight, float pExtensionHeight, float pCollisionHeight, Properties pProperties) {
        super(pNodeWidth, pExtensionWidth, pNodeHeight, pExtensionHeight, pCollisionHeight, pProperties);
    }

    @Inject(
            at = @At("HEAD"),
            method = "hasProperties()Z",
            cancellable = true,
            remap = false,
            require = 0
    )
    public void hasProperties(CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof ICopycatBlock) cir.setReturnValue(false);
    }
}