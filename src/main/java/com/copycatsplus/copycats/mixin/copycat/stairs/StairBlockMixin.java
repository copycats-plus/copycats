package com.copycatsplus.copycats.mixin.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StairBlock.class)
public class StairBlockMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isStairs(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            cancellable = true
    )
    private static void copycatStairs(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(pState.getBlock()) instanceof StairBlock) cir.setReturnValue(true);
    }
}
