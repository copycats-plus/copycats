package com.copycatsplus.copycats.mixin.copycat.wall;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WallBlock.class)
public class WallBlockMixin {

    @Inject(
            at = @At("HEAD"),
            method = "connectsTo(Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/core/Direction;)Z",
            cancellable = true
    )
    private void connectsToCopycat(BlockState pState, boolean pSideSolid, Direction pDirection, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(pState.getBlock()) instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(pState, pDirection))
            cir.setReturnValue(true);
    }

    @Inject(
            at = @At("HEAD"),
            method = "shouldRaisePost(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/shapes/VoxelShape;)Z",
            cancellable = true
    )
    private void raisePostForCopycat(BlockState pState, BlockState pNeighbour, VoxelShape pShape, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(pNeighbour.getBlock()) instanceof WallBlock && pNeighbour.getValue(WallBlock.UP)) {
            cir.setReturnValue(true);
        }
    }
}
