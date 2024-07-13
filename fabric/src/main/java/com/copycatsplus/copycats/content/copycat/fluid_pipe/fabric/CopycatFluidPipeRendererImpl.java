package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;


import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeConnection;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

public abstract class CopycatFluidPipeRendererImpl extends SafeBlockEntityRenderer<CopycatFluidPipeBlockEntity> {

    public CopycatFluidPipeRendererImpl(BlockEntityRendererProvider.Context context) {
    }

    public static void renderSafe(CopycatFluidPipeRenderer renderer, CopycatFluidPipeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                  int light, int overlay) {

        if (ItemBlockRenderTypes.getChunkRenderType(be.getMaterial()) == RenderType.solid())
            return;

        FluidTransportBehaviour pipe = be.getBehaviour(FluidTransportBehaviour.TYPE);
        if (pipe == null)
            return;

        boolean centerOccupied = false;

        for (Direction side : Iterate.directions) {

            PipeConnection.Flow flow = pipe.getFlow(side);
            if (flow == null)
                continue;
            FluidStack fluidStack = flow.fluid;
            if (fluidStack.isEmpty())
                continue;
            LerpedFloat progress = flow.progress;
            if (progress == null)
                continue;

            float value = progress.getValue(partialTicks);
            boolean inbound = flow.inbound;
            if (value == 1) {
                if (inbound) {
                    PipeConnection.Flow opposite = pipe.getFlow(side.getOpposite());
                    if (opposite == null)
                        value -= 1e-6f;
                } else {
                    FluidTransportBehaviour adjacent = BlockEntityBehaviour.get(be.getLevel(), be.getBlockPos()
                            .relative(side), FluidTransportBehaviour.TYPE);
                    if (adjacent == null)
                        value -= 1e-6f;
                    else {
                        PipeConnection.Flow other = adjacent.getFlow(side.getOpposite());
                        if (other == null || !other.inbound && !other.complete)
                            value -= 1e-6f;
                    }
                }
            }

            ExpandedFluidRenderer.renderFluidStream(fluidStack, side, 3 / 16f, value, inbound && !centerOccupied ? 3 / 16f : -3 / 16f, inbound, buffer, ms, light);
            if (inbound) centerOccupied = true;
        }

    }

}

