package com.copycatsplus.copycats.mixin.copycat;

import com.copycatsplus.copycats.content.copycat.IShimCopycatBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ConnectedTextureBehaviour.class, remap = false)
public class ConnectedTextureBehaviourMixin {
    @WrapOperation(
            method = "testConnection(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;II)Z",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;isIgnoredConnectivitySide(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)Z")
    )
    private boolean bypassIfShim(CopycatBlock instance, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, Operation<Boolean> original) {
        if (instance instanceof IShimCopycatBlock shim) {
            return !shim.canConnectTexturesToward(reader, fromPos, toPos, state);
        }

        return original.call(instance, reader, state, face, fromPos, toPos);
    }
}
