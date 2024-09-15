package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Enhance canOcclude checks with custom logic for copycat blocks.
 */
@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {
    @WrapOperation(
            method = "isFaceOccludedByState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z")
    )
    private static boolean canCopycatOcclude(BlockState instance,
                                             Operation<Boolean> original,
                                             @Local(argsOnly = true) BlockGetter level,
                                             @Local(argsOnly = true) BlockPos pos) {
        if (AllBlocks.COPYCAT_BASE.has(instance)) {
            return false;
        }
        if (instance.getBlock() instanceof BracketBlock) {
            return false;
        }
        if (instance.getBlock() instanceof ICopycatBlock copycatBlock) {
            return copycatBlock.canOcclude(level, instance, pos);
        }
        return original.call(instance);
    }
}
