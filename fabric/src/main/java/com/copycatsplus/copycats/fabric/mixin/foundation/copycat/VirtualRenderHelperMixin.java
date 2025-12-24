package com.copycatsplus.copycats.fabric.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.model.fabric.VirtualWorld;
import com.simibubi.create.foundation.utility.fabric.VirtualRenderHelper;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * {@link dev.engine_room.flywheel.lib.model.baked.VirtualBlockGetter} is an abstract class instead of an interface.
 * To allow inheritance of other base classes, a marker interface {@link VirtualWorld} is created as an additional way
 * to indicate a virtual world.
 */
@Mixin(VirtualRenderHelper.class)
public class VirtualRenderHelperMixin {
    @Shadow
    @Final
    private static ThreadLocal<Boolean> forcedVirtualState;

    @Inject(
            method = "isVirtual",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void isVirtual(BlockGetter level, CallbackInfoReturnable<Boolean> cir) {
        if (forcedVirtualState.get() == null && level instanceof VirtualWorld) {
            cir.setReturnValue(true);
        }
    }
}
