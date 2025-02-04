package com.copycatsplus.copycats.mixin.compat.radium;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Make sure Radium's pathfinding algorithm considers slabs properly, since they do not extend from {@link net.minecraft.world.level.block.SlabBlock}.
 */
@ModMixin(requiredMods = {Mods.LITHIUM, Mods.RADIUM})
@Mixin(targets = "me.jellysquid.mods.lithium.common.ai.pathing.PathNodeDefaults")
@Pseudo
public class PathNodeDefaultsMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "getNodeType(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
            require = 0
    )
    private static Block getWrappedBlock(BlockState instance, Operation<Block> original) {
        if (instance.getBlock() instanceof CopycatSlabBlock)
            return Blocks.SMOOTH_STONE_SLAB;
        return original.call(instance);
    }
}
