package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopycatBlock.class)
public abstract class CopycatBlockMixin implements IFunctionalCopycatBlock {
    @Inject(
            method = "getAcceptedBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face, CallbackInfoReturnable<BlockState> cir) {
        if (!(item.getItem() instanceof BlockItem bi))
            return;

        Block block = bi.getBlock();
        if (block instanceof IFunctionalCopycatBlock)
            cir.setReturnValue(null);
    }

    @Inject(
            method = "getMaterial",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getMaterial(BlockGetter reader, BlockPos targetPos, CallbackInfoReturnable<BlockState> cir) {
        if (reader.getBlockEntity(targetPos) instanceof IFunctionalCopycatBlockEntity cbe)
            cir.setReturnValue(cbe.getMaterial());
    }
}
