package com.copycatsplus.copycats.mixin.copycat.trapdoor;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {
    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;"
    )
    private BlockState getWrappedBlock(BlockGetter instance, BlockPos pos, Operation<BlockState> original) {
        BlockState state = original.call(instance, pos);
        if (state.getBlock() instanceof CopycatTrapdoorBlock)
            return CopycatTrapdoorBlock.copyState(state, ICopycatWithWrappedBlock.unwrap(state.getBlock()).defaultBlockState(), true);
        else
            return state;
    }
}
