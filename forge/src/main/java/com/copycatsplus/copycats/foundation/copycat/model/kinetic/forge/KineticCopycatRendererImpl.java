package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.copycatsplus.copycats.utility.forge.ModelDataUtils;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;

public class KineticCopycatRendererImpl {

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        ModelData blockEntityData = ModelDataUtils.mergeData(
                ((BlockEntity) be).getModelData(),
                ModelUtil.VIRTUAL_DATA
        ).build();
        ModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), blockEntityData);
        ModelData.Builder builder = ModelData.builder();
        ModelDataUtils.copyModelData(renderData, builder);
        builder.with(ModelUtil.VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld)
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }
}
