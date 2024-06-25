package com.copycatsplus.copycats.fabric.mixin.copycat.base;

import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.function.Supplier;

@Mixin(CopycatModel.class)
public class CopycatModelMixin {
    @WrapOperation(
            method = "emitBlockQuads",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatModel;emitBlockQuadsInner(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Ljava/util/function/Supplier;Lnet/fabricmc/fabric/api/renderer/v1/render/RenderContext;Lnet/minecraft/world/level/block/state/BlockState;Lcom/simibubi/create/content/decoration/copycat/CopycatModel$CullFaceRemovalData;Lcom/simibubi/create/content/decoration/copycat/CopycatModel$OcclusionData;)V")
    )
    private void fixCT(CopycatModel instance, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, @Coerce Object cullFaceRemovalData, @Coerce Object occlusionData, Operation<Void> original) {
        if (state.getBlock() instanceof CopycatBlock copycatBlock) {
            FilteredBlockAndTintGetter filteredBlockAndTintGetter = new FilteredBlockAndTintGetter(blockView, t -> {
                BlockEntity be = blockView.getBlockEntity(pos);
                if (be instanceof CTCopycatBlockEntity ctbe)
                    if (!ctbe.isCTEnabled())
                        return false;
                return copycatBlock.canConnectTexturesToward(blockView, pos, t, state);
            });
            original.call(instance, filteredBlockAndTintGetter, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        } else {
            original.call(instance, blockView, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        }
    }
}
