package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * Implementation for {@link ICustomCTBlocking}.
 * <p>
 * 1.18.2: Redirect calls to getConnectiveMaterial to getAppearance
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

    @Inject(
            method = "getCTBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getAppearance(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockState = reader.getBlockState(toPos);

        if (blockState.getBlock() instanceof IMultiStateCopycatBlock ufb) {
            BlockState connectiveMaterial = IMultiStateCopycatBlock.getAppearance(ufb, blockState, reader, toPos, face, reference, fromPos);
            cir.setReturnValue(connectiveMaterial == null ? blockState : connectiveMaterial);
            return;
        }

        if (blockState.getBlock() instanceof ICopycatBlock ufb) {
            BlockState connectiveMaterial = ICopycatBlock.getAppearance(ufb, blockState, reader, toPos, face, reference, fromPos);
            cir.setReturnValue(connectiveMaterial == null ? blockState : connectiveMaterial);
        }
    }

    @Inject(
            method = "isBeingBlocked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isFaceFull(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, CallbackInfoReturnable<Boolean> cir) {
        Direction face2 = face.getOpposite();
        BlockPos blockingPos = otherPos.relative(face2);
        BlockState otherState = reader.getBlockState(otherPos);
        BlockState blockingState = reader.getBlockState(blockingPos);
        if (blockingState.getBlock() instanceof ICopycatBlock) {
            if (!BlockFaceUtils.faceMatch(reader, otherState, otherPos, blockingState, blockingPos, face2)) {
                cir.setReturnValue(false);
                return;
            }
        }
        if (!Block.isFaceFull(blockingState.getShape(reader, otherPos.relative(face)), face2)) {
            cir.setReturnValue(false);
        }
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

    @WrapOperation(
            method = "testConnection",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;isIgnoredConnectivitySide(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean toggleCT(CopycatBlock instance, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, Operation<Boolean> original) {
        BlockEntity be = reader.getBlockEntity(fromPos);
        if (be instanceof ICopycatBlockEntity ctbe) {
            if (!ctbe.isCTEnabled()) return true;
        }

        return original.call(instance, reader, state, face, fromPos, toPos);
    }
}
