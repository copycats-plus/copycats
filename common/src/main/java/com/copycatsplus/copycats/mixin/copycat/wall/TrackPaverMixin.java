package com.copycatsplus.copycats.mixin.copycat.wall;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.simibubi.create.content.trains.track.TrackPaver;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TrackPaver.class, remap = false)
public class TrackPaverMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isWallLike(Lnet/minecraft/world/level/block/state/BlockState;)Z",
            cancellable = true
    )
    private static void copycatIsWallLike(BlockState defaultBlockState, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(defaultBlockState.getBlock()) instanceof WallBlock)
            cir.setReturnValue(true);
    }
}
