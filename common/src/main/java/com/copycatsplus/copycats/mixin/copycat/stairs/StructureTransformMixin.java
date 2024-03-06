package com.copycatsplus.copycats.mixin.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTransform.class)
public class StructureTransformMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"),
            method = "apply(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/level/block/state/BlockState;"
    )
    private Block getWrappedBlock(BlockState instance, Operation<Block> original) {
        return ICopycatWithWrappedBlock.unwrap(original.call(instance));
    }
}
