package com.copycatsplus.copycats.mixin.copycat;

import com.copycatsplus.copycats.content.copycat.CTCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CopycatModel.class)
public class CopycatModelMixin {
    @Inject(
            at = @At("HEAD"),
           method = "gatherOcclusionData"
           /* method = "lambda$gatherModelData$0(Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)Z"*/,
            cancellable = true
    )
    private void considerCT(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material, @Coerce Object occlusionData, CopycatBlock copycatBlock, CallbackInfo ci) {
        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof CTCopycatBlockEntity ctbe)) return;
        if (!ctbe.isCTEnabled()) ci.cancel();
    }
}
