package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Implementation for {@link ICustomCTBlocking}.
 */
@Mixin(value = ConnectedTextureBehaviour.class)
public class ConnectedTextureBehaviourMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isBeingBlocked",
            cancellable = true
    )
    private void isCopycatBlockable(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        if (reader instanceof FilteredBlockAndTintGetter accessor) {
            reader = accessor.wrapped; // get the true reader, not the one filtered by copycats
        }
        if (reader instanceof ScaledBlockAndTintGetter accessor) {
            reader = accessor.getWrapped();
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

    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;isFaceFull(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/core/Direction;)Z"),
            method = "isBeingBlocked"
    )
    private boolean isFaceFull(VoxelShape shape, Direction face, Operation<Boolean> original, BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos,
                               Direction face2) {
        BlockPos blockingPos = otherPos.relative(face2);
        BlockState otherState = reader.getBlockState(otherPos);
        BlockState blockingState = reader.getBlockState(blockingPos);
        if (blockingState.getBlock() instanceof ICopycatBlock)
            return BlockFaceUtils.faceMatch(reader, otherState, otherPos, blockingState, blockingPos, face2);
        return original.call(shape, face);
    }


    @WrapOperation(
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/block/connected/ConnectedTextureBehaviour;getCTBlockState(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"),
            method = "isBeingBlocked"
    )
    private BlockState getAppearanceForBlockingLogic(ConnectedTextureBehaviour instance, BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, Operation<BlockState> original) {
        CopycatExternalContext.setForBlockingLogic(true);
        BlockState state = original.call(instance, reader, reference, face, fromPos, toPos);
        CopycatExternalContext.setForBlockingLogic(false);
        return state;
    }
}
