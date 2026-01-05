package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Enhance canOcclude checks with custom logic for copycat blocks.
 */
@Mixin(Block.class)
public class BlockMixin {
    @WrapOperation(
            method = "shouldRenderFace",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;canOcclude()Z")
    )
    private static boolean canCopycatOcclude(BlockState instance,
                                             Operation<Boolean> original,
                                             @Share("copycat$blockState") LocalRef<BlockState> stateRef,
                                             @Local(argsOnly = true) BlockGetter level,
                                             @Local(argsOnly = true, ordinal = 1) BlockPos pos) {
        if (AllBlocks.COPYCAT_BASE.has(instance)) {
            return false;
        }
        if (instance.getBlock() instanceof BracketBlock) {
            return false;
        }
        if (instance.getBlock() instanceof ICopycatBlock copycatBlock) {
            if (copycatBlock.canOcclude(level, instance, pos)) {
                stateRef.set(instance);
                return true;
            }
            return false;
        }
        return original.call(instance);
    }

    @Inject(
            method = "shouldRenderFace",
            at = @At(value = "NEW", target = "net/minecraft/world/level/block/Block$BlockStatePairKey"),
            cancellable = true
    )
    private static void calculateOcclusionShape(BlockState state, BlockGetter level, BlockPos offset, Direction face, BlockPos pos,
                                                CallbackInfoReturnable<Boolean> cir, @Share("copycat$blockState") LocalRef<BlockState> stateRef) {
        BlockState blockState = stateRef.get();
        if (blockState != null && blockState.getBlock() instanceof ICopycatBlock copycatBlock) {
            Optional<Boolean> result = copycatBlock.shapeCanOccludeNeighbor(level, pos, blockState, offset, face.getOpposite()).map(b -> !b);
            result.ifPresent(cir::setReturnValue);
        }
    }
}
