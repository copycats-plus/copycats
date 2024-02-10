package com.copycatsplus.copycats.mixin.copycat;

import com.copycatsplus.copycats.content.copycat.CTCopycatBlockEntity;

import com.copycatsplus.copycats.content.copycat.ICustomCTBlocking;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.level.block.entity.BlockEntity;

@Mixin(value = ConnectedTextureBehaviour.class, remap = false)
public class ConnectedTextureBehaviourMixin {
    @WrapOperation(
            method = "testConnection(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;II)Z",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;isIgnoredConnectivitySide(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean bypassIfShim(CopycatBlock instance, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, Operation<Boolean> original) {
        if (instance instanceof IShimCopycatBlock ctBlock) {
            BlockEntity be = reader.getBlockEntity(fromPos);
            if (be instanceof CTCopycatBlockEntity ctbe) {
                if (!ctbe.isCTEnabled()) return true;
            }
            return !ctBlock.canConnectTexturesToward(reader, fromPos, toPos, state);
        }

        return original.call(instance, reader, state, face, fromPos, toPos);
    }
    @Inject(
            at = @At("HEAD"),
            method = "isBeingBlocked(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",
            cancellable = true
    )
    private void isCopycatBlockable(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (reader instanceof FilteredBlockAndTintGetterAccessor accessor) {
            reader = accessor.getWrapped(); // get the true reader, not the one filtered by copycats
        }
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
