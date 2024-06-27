package com.copycatsplus.copycats.content.copycat.base.model.functional.forge;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.functional.WrappedRenderWorld;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import static com.copycatsplus.copycats.content.copycat.base.model.functional.forge.BakedModelWithDataBuilder.VIRTUAL_PROPERTY;

public class FunctionalCopycatRenderHelperImpl {

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, IFunctionalCopycatBlockEntity be, PoseStack ms) {
        WrappedRenderWorld renderWorld = new WrappedRenderWorld(be).setCTMode(true);
        IModelData renderData = model.getModelData(renderWorld, be.getBlockPos(), be.getBlockState(), be.getCopycatBlockEntity().getModelData());
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        copyModelData(renderData, builder);
        builder.withInitial(VIRTUAL_PROPERTY, true);

        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(renderWorld.setCTMode(false))
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .withData(builder.build())
                .build();
    }

    public static ModelDataMap.Builder mergeData(IModelData data1, IModelData data2) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        copyModelData(data1, builder);
        copyModelData(data2, builder);
        return builder;
    }

    static void copyModelData(IModelData from, ModelDataMap.Builder to) {
        //Todo: needs fixing
/*        for (ModelProperty<?> property : from.getProperties()) {
            copyModelProperty(to, from, property);
        }*/
    }

    static <T> void copyModelProperty(ModelDataMap.Builder to, IModelData from, ModelProperty<T> property) {
        to.withInitial(property, from.getData(property));
    }
}
