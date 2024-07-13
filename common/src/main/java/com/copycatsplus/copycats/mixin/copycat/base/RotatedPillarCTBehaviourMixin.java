package com.copycatsplus.copycats.mixin.copycat.base;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allow rotated pillar blocks to connect textures to our copycats.
 */
@Mixin(RotatedPillarCTBehaviour.class)
public class RotatedPillarCTBehaviourMixin {
    @Inject(
            method = "connectsTo",
            at = @At("HEAD"),
            cancellable = true
    )
    private void connectsToCopycats(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset, CallbackInfoReturnable<Boolean> cir) {
        if (reader.getBlockState(pos).getBlock() instanceof ICopycatBlock) {
            cir.setReturnValue(true);
            return;
        }
        if (reader.getBlockState(otherPos).getBlock() instanceof ICopycatBlock) {
            cir.setReturnValue(true);
            return;
        }
    }
}
