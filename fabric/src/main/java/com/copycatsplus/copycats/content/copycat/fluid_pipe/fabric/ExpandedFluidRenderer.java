package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.utility.AngleHelper;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

@SuppressWarnings("UnstableApiUsage")
public class ExpandedFluidRenderer {
    public static VertexConsumer getFluidBuilder(MultiBufferSource buffer) {
        return buffer.getBuffer(RenderTypes.getFluid());
    }

    public static void renderFluidStream(FluidStack fluidStack, Direction direction, float radius, float progress, float centerOffset,
                                         boolean inbound, MultiBufferSource buffer, PoseStack ms, int light) {
        renderFluidStream(fluidStack, direction, radius, progress, centerOffset, inbound, getFluidBuilder(buffer), ms, light);
    }

    public static void renderFluidStream(FluidStack fluidStack, Direction direction, float radius, float progress, float centerOffset,
                                         boolean inbound, VertexConsumer builder, PoseStack ms, int light) {
        FluidVariant fluidVariant = fluidStack.getType();
        TextureAtlasSprite[] sprites = FluidVariantRendering.getSprites(fluidVariant);
        if (sprites == null) {
            return;
        }
        TextureAtlasSprite flowTexture = sprites[1];
        TextureAtlasSprite stillTexture = sprites[0];

        int color = FluidVariantRendering.getColor(fluidVariant);
        int blockLightIn = (light >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, FluidVariantAttributes.getLuminance(fluidVariant));
        light = (light & 0xF00000) | luminosity << 4;

        if (inbound)
            direction = direction.getOpposite();

        TransformStack msr = TransformStack.cast(ms);
        ms.pushPose();
        msr.centre()
                .rotateY(AngleHelper.horizontalAngle(direction))
                .rotateX(direction == Direction.UP ? 180 : direction == Direction.DOWN ? 0 : 270)
                .unCentre();
        ms.translate(.5, 0, .5);

        float h = radius;
        float hMin = -radius;
        float hMax = radius;
        float y = inbound ? 1 : (.5f + centerOffset);
        float yMin = y - Mth.clamp(progress * (.5f + centerOffset), 0, 1);
        float yMax = y;

        for (int i = 0; i < 4; i++) {
            ms.pushPose();
            FluidRenderer.renderFlowingTiledFace(Direction.SOUTH, hMin, yMin, hMax, yMax, h, builder, ms, light, color, flowTexture);
            ms.popPose();
            msr.rotateY(90);
        }

        if (progress != 1)
            FluidRenderer.renderStillTiledFace(Direction.DOWN, hMin, hMin, hMax, hMax, yMin, builder, ms, light, color, stillTexture);

        ms.popPose();
    }
}
