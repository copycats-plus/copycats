package com.copycatsplus.copycats.mixin.copycat.fence;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceBlock.class)
public class FenceBlockMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isSameFence(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            cancellable = true
    )
    private void connectToCopycatFence(BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        // Normally `this` is a wrapped fence while `pState` is a copycat fence
        // But other checks are added in case someone wanna use the wrapped fence by itself
        if (pState.is(BlockTags.FENCES) &&
                (((FenceBlock) (Object) this).defaultBlockState().is(CCBlocks.COPYCAT_FENCE.get()) ||
                        ((FenceBlock) (Object) this).defaultBlockState().is(CCBlocks.WRAPPED_COPYCAT_FENCE.get()) ||
                        pState.is(CCBlocks.COPYCAT_FENCE.get()) ||
                        pState.is(CCBlocks.WRAPPED_COPYCAT_FENCE.get())))
            cir.setReturnValue(true);
    }

    @Inject(
            at = @At("HEAD"),
            method = "connectsTo(Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/core/Direction;)Z",
            cancellable = true
    )
    private void connectsToCopycat(BlockState pState, boolean pIsSideSolid, Direction pDirection, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(pState.getBlock()) instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(pState, pDirection))
            cir.setReturnValue(true);
    }
}
