package com.copycatsplus.copycats.mixin.compat.radium;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "me.jellysquid.mods.lithium.common.ai.pathing.PathNodeDefaults")
@Pseudo // Would normally fail if Radium is not installed
public class PathNodeDefaultsMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "getNodeType(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;"
    )
    private static Block getWrappedBlock(BlockState instance, Operation<Block> original) {
        return ICopycatWithWrappedBlock.unwrap(original.call(instance));
    }
}
