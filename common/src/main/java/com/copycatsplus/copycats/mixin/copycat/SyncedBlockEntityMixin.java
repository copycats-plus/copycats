package com.copycatsplus.copycats.mixin.copycat;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for Create 6.0.7 and up, where calling `notifyUpdate` on a virtual world causes a crash due to getChunk being
 * unimplemented in VirtualRenderWorld
 */
@Mixin(SyncedBlockEntity.class)
public class SyncedBlockEntityMixin {
    @Inject(
            method = "notifyUpdate()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void doNotSyncVirtual(CallbackInfo ci) {
        SyncedBlockEntity self = (SyncedBlockEntity) (Object) this;
        if (self.getLevel() != null && self.getLevel() instanceof VirtualRenderWorld) {
            ci.cancel();
        }
    }
}
