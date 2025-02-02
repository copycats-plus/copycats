package com.copycatsplus.copycats.content.copycat.fluid_pipe.fabric;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.fabric.CopycatModelFabric;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CopycatFluidPipeModelFabric extends CopycatModelFabric {

    public CopycatFluidPipeModelFabric(BakedModel originalModel, CopycatModelCore core, boolean disableAO) {
        super(originalModel, core, disableAO);
    }

    @Override
    protected void prepareModelCore(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, BlockState material, Object renderAttachmentData) {
        super.prepareModelCore(blockView, state, pos, randomSupplier, material, renderAttachmentData);
        CopycatFluidPipeModelCore.PipeModelData data = new CopycatFluidPipeModelCore.PipeModelData();
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(blockView, pos, BracketedBlockEntityBehaviour.TYPE);

        if (renderAttachmentData instanceof FluidTransportBehaviour.AttachmentTypes[] attachments) {
            for (int i = 0; i < attachments.length; i++) {
                data.putAttachment(Iterate.directions[i], attachments[i]);
            }
        }

        if (bracket != null)
            data.putBracket(bracket.getBracket());

        data.setEncased(FluidPipeBlock.shouldDrawCasing(blockView, pos, state));

        if (core instanceof CopycatModelCore.WithData<?>) {
            @SuppressWarnings("unchecked")
            CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData> dataCore = (CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData>) core;
            dataCore.setData(data);
        }
    }
}
