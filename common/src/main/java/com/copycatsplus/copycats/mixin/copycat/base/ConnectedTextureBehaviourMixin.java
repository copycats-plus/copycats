package com.copycatsplus.copycats.mixin.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = ConnectedTextureBehaviour.class)
public class ConnectedTextureBehaviourMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isBeingBlocked",
            cancellable = true
    )
    private void isCopycatBlockable(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        BlockState selfState = reader.getBlockState(pos);
        BlockPos blockingPos = otherPos.relative(face);
        if (selfState.getBlock() instanceof ICustomCTBlocking customBlocking) {
            Optional<Boolean> blocked = customBlocking.isCTBlocked(reader, selfState, pos, otherPos, blockingPos, face);
            if (blocked.isPresent()) {
                cir.setReturnValue(blocked.get());
                return;
            }
        }
        BlockState blockingState = reader.getBlockState(blockingPos);
        if (blockingState.getBlock() instanceof ICustomCTBlocking customBlocker) {
            Optional<Boolean> blocking = customBlocker.blockCTTowards(reader, blockingState, blockingPos, pos, otherPos, face.getOpposite());
            blocking.ifPresent(cir::setReturnValue);
        }
    }
}
