package com.copycatsplus.copycats.fabric.mixin.copycat.base;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CopycatModel.class, remap = false)
public class CopycatModelMixin {

  /*  //Toggles Rendering 😂
    @Inject(
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatModel;gatherOcclusionData(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lcom/simibubi/create/content/decoration/copycat/CopycatModel$OcclusionData;Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;)V", shift = At.Shift.BEFORE),
            method = "emitBlockQuads",
            cancellable = true
    )

    private void considerCT(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, CallbackInfo ci) {
        BlockEntity be = blockView.getBlockEntity(pos);
        if (!(be instanceof CTCopycatBlockEntity ctbe)) return;
        if (!ctbe.isCTEnabled()) ci.cancel();
    }*/
}
