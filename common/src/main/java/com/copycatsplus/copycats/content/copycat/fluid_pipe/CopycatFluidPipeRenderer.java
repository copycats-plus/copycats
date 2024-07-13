package com.copycatsplus.copycats.content.copycat.fluid_pipe;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CopycatFluidPipeRenderer extends SafeBlockEntityRenderer<CopycatFluidPipeBlockEntity> {

    public CopycatFluidPipeRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        renderSafe(this, be, partialTicks, ms, buffer, light, overlay);
    }

    @ExpectPlatform
    public static void renderSafe(CopycatFluidPipeRenderer renderer, CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                  int light, int overlay) {

    }
}

