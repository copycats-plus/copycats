package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.block.connected.CTModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CTModel.class)
public class CTModelMixin {
    @WrapOperation(
            method = "createCTData",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;")
    )
    private Block createCTData(BlockState instance, Operation<Block> original) {
        if (instance.getBlock() instanceof IFunctionalCopycatBlock fcb) {
            CCBlocks.WRAPPED_COPYCAT.get().setWrapped(fcb);
            return CCBlocks.WRAPPED_COPYCAT.get();
        }
        return original.call(instance);
    }
}