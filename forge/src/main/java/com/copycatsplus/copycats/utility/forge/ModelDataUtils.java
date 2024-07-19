package com.copycatsplus.copycats.utility.forge;

import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class ModelDataUtils {
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
