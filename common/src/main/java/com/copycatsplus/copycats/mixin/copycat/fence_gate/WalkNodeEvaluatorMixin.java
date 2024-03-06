package com.copycatsplus.copycats.mixin.copycat.fence_gate;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WalkNodeEvaluator.class, priority = 1100)
// A higher priority is needed so that this is attempted AFTER another mod overwrites the method
public class WalkNodeEvaluatorMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "getBlockPathTypeRaw(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
            require = 0 // Fail silently if target is overwritten by optimization mods
    )
    private static Block getWrappedBlock(BlockState instance, Operation<Block> original) {
        return ICopycatWithWrappedBlock.unwrap(original.call(instance));
    }
}
