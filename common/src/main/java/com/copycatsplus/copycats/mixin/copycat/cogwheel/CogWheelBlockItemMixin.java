package com.copycatsplus.copycats.mixin.copycat.cogwheel;

import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Skip the placement helper in cogwheels if the player is trying to fill a copycat cogwheel
 */
@Mixin(value = CogwheelBlockItem.class, priority = 1200)
// Use a higher priority because this may be overwritten by other mods
public class CogWheelBlockItemMixin {
    @Inject(
            method = "onItemUseFirst",
            at = @At("HEAD"),
            cancellable = true,
            require = 0
    )
    private void disablePlacementForCopycat(ItemStack stack, UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Block block = ((BlockItem) (Object) this).getBlock();
        if (block instanceof ICopycatBlock)
            return;
        if (!(block instanceof CogWheelBlock cogWheelBlock))
            return;
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof CopycatCogWheelBlock copycatBlock) {
            String property = copycatBlock.getPropertyFromInteraction(state, world, pos, context.getClickLocation(), context.getClickedFace(), true);
            if (property.equals(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName())
                    && AllBlocks.COPYCAT_BASE.has(IMultiStateCopycatBlock.getMaterial(world, pos, property))
                    && cogWheelBlock.isLargeCog() == copycatBlock.isLargeCog()) {
                cir.setReturnValue(InteractionResult.PASS);
            }
        }
    }
}
