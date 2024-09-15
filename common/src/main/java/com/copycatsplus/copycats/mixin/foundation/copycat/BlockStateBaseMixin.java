package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public class BlockStateBaseMixin {
    @Inject(
            method = "canOcclude",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customOcclusion(CallbackInfoReturnable<Boolean> cir) {
        BlockState instance = (BlockState) (Object) this;
        if (instance.getBlockHolder().is(Mods.CREATE.rl("copycat_base"))) {
            cir.setReturnValue(false);
        }
        if (instance.getBlock() instanceof BracketBlock) {
            cir.setReturnValue(false);
        }
    }
}
