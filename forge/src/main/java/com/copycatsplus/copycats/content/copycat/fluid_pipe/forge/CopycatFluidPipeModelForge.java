package com.copycatsplus.copycats.content.copycat.fluid_pipe.forge;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.forge.CopycatModelForge;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeModelCore;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

public class CopycatFluidPipeModelForge extends CopycatModelForge {

    private static final ModelProperty<CopycatFluidPipeModelCore.PipeModelData> PIPE_PROPERTY = new ModelProperty<>();

    public CopycatFluidPipeModelForge(BakedModel originalModel, CopycatModelCore core, boolean disableAO) {
        super(originalModel, core, disableAO);
    }

    @Override
    public ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                             ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        CopycatFluidPipeModelCore.PipeModelData data = new CopycatFluidPipeModelCore.PipeModelData();
        FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, FluidTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);

        if (transport != null)
            for (Direction d : Iterate.directions)
                data.putAttachment(d, transport.getRenderedRimAttachment(world, pos, state, d));
        if (bracket != null)
            data.putBracket(bracket.getBracket());

        data.setEncased(FluidPipeBlock.shouldDrawCasing(world, pos, state));
        return builder.with(PIPE_PROPERTY, data);
    }

    @Override
    protected void prepareModelCore(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        super.prepareModelCore(state, rand, data);
        if (core instanceof CopycatModelCore.WithData<?>) {
            @SuppressWarnings("unchecked")
            CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData> dataCore = (CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData>) core;
            CopycatFluidPipeModelCore.PipeModelData pipeData = data.get(PIPE_PROPERTY);
            if (pipeData == null)
                pipeData = new CopycatFluidPipeModelCore.PipeModelData();
            dataCore.setData(pipeData);
        }
    }
}
