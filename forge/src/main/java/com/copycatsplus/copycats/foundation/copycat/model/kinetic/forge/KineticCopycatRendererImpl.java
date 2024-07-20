package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.copycatsplus.copycats.forge.mixin.copycat.base.ModelDataMapAccessor;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.copycatsplus.copycats.utility.forge.ModelDataUtils;
import com.copycatsplus.copycats.utility.forge.ModelUtils;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;

import java.util.Set;

public class KineticCopycatRendererImpl {

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        IModelData blockEntityData = ModelDataUtils.mergeData(
                ((BlockEntity) be).getModelData(),
                ModelUtils.VIRTUAL_DATA
        ).build();
        IModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), blockEntityData);
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        ModelDataUtils.copyModelData(renderData, builder);
        builder.withInitial(ModelUtils.VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld)
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }
}
