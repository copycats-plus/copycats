package com.copycatsplus.copycats.mixin.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CopycatBlock.class)
public class CopycatBlockMixin {

    @Inject(method = "getAcceptedBlockState", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    private void copycats$alwaysDenyMultiStateBlock(Level pLevel, BlockPos pPos, ItemStack item, Direction face, CallbackInfoReturnable<BlockState> cir, BlockItem bi, Block block) {
        if (block instanceof MultiStateCopycatBlock)
            cir.setReturnValue(null);
    }
}
