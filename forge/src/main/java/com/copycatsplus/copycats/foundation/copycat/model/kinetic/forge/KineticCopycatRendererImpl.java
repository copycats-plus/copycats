package com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class KineticCopycatRendererImpl {

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be);
        ModelData blockEntityData = mergeData(
                ((BlockEntity) be).getModelData(),
                ModelUtil.VIRTUAL_DATA
        ).build();
        ModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), blockEntityData);
        ModelData.Builder builder = ModelData.builder();
        copyModelData(renderData, builder);
        builder.with(ModelUtil.VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld)
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }

    public static ModelData.Builder mergeData(ModelData data1, ModelData data2) {
        ModelData.Builder builder = ModelData.builder();
        copyModelData(data1, builder);
        copyModelData(data2, builder);
        return builder;
    }

    public static void copyModelData(ModelData from, ModelData.Builder to) {
        for (ModelProperty<?> property : from.getProperties()) {
            copyModelProperty(to, from, property);
        }
    }

    static <T> void copyModelProperty(ModelData.Builder to, ModelData from, ModelProperty<T> property) {
        to.with(property, from.get(property));
    }
}
