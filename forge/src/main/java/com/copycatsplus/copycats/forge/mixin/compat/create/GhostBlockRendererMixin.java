package com.copycatsplus.copycats.forge.mixin.compat.create;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(targets = "com.simibubi.create.foundation.utility.ghost.GhostBlockRenderer$TransparentGhostBlockRenderer")
public class GhostBlockRendererMixin {
    @WrapOperation(
            method = "renderModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BakedModel;getQuads(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/util/RandomSource;Lnet/minecraftforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)Ljava/util/List;"),
            require = 0 // in case Create fixes this in the future
    )
    private List<BakedQuad> renderModelWithoutRenderType(BakedModel instance,
                                                         @Nullable BlockState state,
                                                         @Nullable Direction side,
                                                         @NotNull RandomSource rand,
                                                         @NotNull ModelData data,
                                                         @Nullable RenderType renderType,
                                                         Operation<List<BakedQuad>> original) {
        return original.call(instance, state, side, rand, data, null);
    }
}
