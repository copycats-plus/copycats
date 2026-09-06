package com.copycatsplus.copycats.mixin.compat.sodium;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.copycatsplus.copycats.foundation.copycat.ICopycatCullable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Makes sure that copycat blocks are not occluded by Rubidium
 */
@ModMixin(requiredMods = Mods.SODIUM)
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache")
@Pseudo
public class BlockOcclusionCacheMixin {
    @WrapOperation(
            method = "shouldDrawSide",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z")
    )
    private boolean canCopycatOcclude(BlockState instance, Operation<Boolean> original,
                                      @Local(argsOnly = true) BlockGetter level,
                                      @Local BlockPos.MutableBlockPos otherPos) {
        if (AllBlocks.COPYCAT_BASE.has(instance)) {
            return false;
        }
        Block block = instance.getBlock();
        if (block instanceof ICopycatCullable copycatBlock) {
            // IMultiStateCopycatBlock always returns false
            return copycatBlock.canOcclude(level, instance, otherPos);
        }
        return original.call(instance);
    }
}
