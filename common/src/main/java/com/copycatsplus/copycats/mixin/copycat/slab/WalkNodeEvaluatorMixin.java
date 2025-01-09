package com.copycatsplus.copycats.mixin.copycat.slab;

import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Make sure the vanilla pathfinding algorithm considers slabs properly, since they do not extend from {@link net.minecraft.world.level.block.SlabBlock}.
 *
 * A Radium compatible version is available in {@link com.copycatsplus.copycats.mixin.compat.radium.PathNodeDefaultsMixin}.
 */
@Mixin(value = WalkNodeEvaluator.class, priority = 1100)
// A higher priority is needed so that this is attempted AFTER another mod overwrites the method
public class WalkNodeEvaluatorMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "getBlockPathTypeRaw",
            require = 0 // Fail silently if target is overwritten by optimization mods
    )
    private static Block getWrappedBlock(BlockState instance, Operation<Block> original) {
        if (instance.getBlock() instanceof CopycatSlabBlock)
            return Blocks.SMOOTH_STONE_SLAB;
        return original.call(instance);
    }
}
