package com.copycatsplus.copycats.fabric.mixin.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

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
