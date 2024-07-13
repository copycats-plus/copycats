package com.copycatsplus.copycats.mixin.copycat.slab;

import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.actors.seat.SeatMovementBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Consider copycat slab as a normal slab when handling seat movement.
 */
@Mixin(SeatMovementBehaviour.class)
public class SeatMovementBehaviourMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "visitNewPosition"
    )
    private Block getSlab(BlockState instance, Operation<Block> original) {
        if (instance.getBlock() instanceof CopycatSlabBlock)
            return Blocks.SMOOTH_STONE_SLAB;
        return original.call(instance);
    }
}
