package com.copycatsplus.copycats.content.copycat.fluid_pipe.forge;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Function;

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
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
        FluidType fluidAttributes = fluid.getFluidType();
        Function<ResourceLocation, TextureAtlasSprite> spriteAtlas = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite flowTexture = spriteAtlas.apply(clientFluid.getFlowingTexture(fluidStack));
        TextureAtlasSprite stillTexture = spriteAtlas.apply(clientFluid.getStillTexture(fluidStack));

        int color = clientFluid.getTintColor(fluidStack);
        int blockLightIn = (light >> 4) & 0xF;
        int luminosity = Math.max(blockLightIn, fluidAttributes.getLightLevel(fluidStack));
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
