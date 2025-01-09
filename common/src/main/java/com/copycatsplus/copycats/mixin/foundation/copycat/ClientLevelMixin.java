package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(
            method = "addDestroyBlockEffect",
            at = @At("HEAD"),
            cancellable = true
    )
    private void destroyEffectForCopycats(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.getBlock() instanceof ICopycatBlock) {
            ClientLevel level = (ClientLevel) (Object) this;
            Minecraft.getInstance().particleEngine.destroy(pos, ICopycatBlock.getMaterial(level, pos));
            ci.cancel();
        }
    }
}
