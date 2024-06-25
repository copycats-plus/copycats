package com.copycatsplus.copycats.mixin.compat.rubidium;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateRenderManager;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateTextureAtlasSprite;
import me.jellysquid.mods.sodium.client.model.color.ColorProvider;
import me.jellysquid.mods.sodium.client.model.quad.BakedQuadView;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderer.class)
@Pseudo
public class BlockRendererMixin {
    @Inject(
            method = "getVertexColors",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/model/color/ColorProvider;getColors(Lme/jellysquid/mods/sodium/client/world/WorldSlice;Lnet/minecraft/core/BlockPos;Ljava/lang/Object;Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;[I)V"
            )
    )
    private void beforeColor(BlockRenderContext ctx, ColorProvider<BlockState> colorProvider, BakedQuadView quad, CallbackInfoReturnable<int[]> cir) {
        if (quad.getSprite() instanceof MultiStateTextureAtlasSprite sprite)
            MultiStateRenderManager.setRenderingProperty(sprite.getProperty());
    }

    @Inject(
            method = "getVertexColors",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/model/color/ColorProvider;getColors(Lme/jellysquid/mods/sodium/client/world/WorldSlice;Lnet/minecraft/core/BlockPos;Ljava/lang/Object;Lme/jellysquid/mods/sodium/client/model/quad/ModelQuadView;[I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterColor(BlockRenderContext ctx, ColorProvider<BlockState> colorProvider, BakedQuadView quad, CallbackInfoReturnable<int[]> cir) {
        MultiStateRenderManager.setRenderingProperty(null);
    }
}
