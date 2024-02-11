package com.copycatsplus.copycats.mixin.compat.diagonalfences;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.copycatsplus.copycats.content.copycat.fence.WrappedFenceBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.FenceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This patch has to be applied after Diagonal Fences modifies the FenceBlock class
 */
@Mixin(value = FenceBlock.class, priority = 1100)
public abstract class FenceBlockMixin extends CrossCollisionBlock {

    public FenceBlockMixin(float pNodeWidth, float pExtensionWidth, float pNodeHeight, float pExtensionHeight, float pCollisionHeight, Properties pProperties) {
        super(pNodeWidth, pExtensionWidth, pNodeHeight, pExtensionHeight, pCollisionHeight, pProperties);
    }

    /**
     * Makes sure that copycat fences are not processed by Diagonal Fences
     */
    @Inject(
            at = @At("HEAD"),
            method = "hasProperties()Z",
            cancellable = true,
            remap = false,
            require = 0, expect = 0
    )
    public void hasProperties(CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(this) instanceof WrappedFenceBlock) cir.setReturnValue(false);
    }
}
