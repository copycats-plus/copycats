package com.copycatsplus.copycats.mixin.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.IShimCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConnectedTextureBehaviour.class)
public abstract class ConnectedTextureBehaviourMixin {

    @Inject(
            method = "getCTBlockState",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getCTBlockState(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState blockState = reader.getBlockState(toPos);

        if (blockState.getBlock() instanceof IFunctionalCopycatBlock ufb) {
            BlockState connectiveMaterial = ufb.getConnectiveMaterial(reader, reference, face, fromPos, toPos);
            cir.setReturnValue(connectiveMaterial == null ? blockState : connectiveMaterial);
        }
    }

    @Inject(
            method = "testConnection",
            at = @At("HEAD"),
            cancellable = true
    )
    private void functionalCT(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face, Direction horizontal, Direction vertical, int sh, int sv, CallbackInfoReturnable<Boolean> cir) {
        BlockPos p = pos.relative(horizontal, sh)
                .relative(vertical, sv);
        BlockState blockState = reader.getBlockState(pos);

        if (blockState.getBlock() instanceof IShimCopycatBlock shim) {
            BlockEntity be = reader.getBlockEntity(pos);
            if (be instanceof CTCopycatBlockEntity ctbe) {
                if (!ctbe.isCTEnabled()) {
                    cir.setReturnValue(false);
                    return;
                }
            }
            cir.setReturnValue(shim.canConnectTexturesToward(reader, pos, p, blockState) &&
                    ((ConnectedTextureBehaviour) (Object) this).connectsTo(state, ((ConnectedTextureBehaviour) (Object) this).getCTBlockState(reader, blockState, face, pos, p), reader, pos, p, face,
                    sh == 0 ? null : sh == -1 ? horizontal.getOpposite() : horizontal,
                    sv == 0 ? null : sv == -1 ? vertical.getOpposite() : vertical));
            return;
        }

        if (blockState.getBlock() instanceof IFunctionalCopycatBlock ufb
                && ufb.isIgnoredConnectivitySide(reader, blockState, face, pos, p))
            cir.setReturnValue(false);
    }
}
