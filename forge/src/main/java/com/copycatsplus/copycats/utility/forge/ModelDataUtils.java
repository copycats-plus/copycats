package com.copycatsplus.copycats.utility.forge;

import com.copycatsplus.copycats.forge.mixin.foundation.copycat.ModelDataMapAccessor;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Set;

public class ModelDataUtils {
    public static ModelDataMap.Builder mergeData(IModelData data1, IModelData data2) {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();
        copyModelData(data1, builder);
        copyModelData(data2, builder);
        return builder;
    }

    public static void copyModelData(IModelData from, ModelDataMap.Builder to) {
        for (ModelProperty<?> property : getProperties(from)) {
            copyModelProperty(to, from, property);
        }
    }

    static Set<ModelProperty<?>> getProperties(IModelData data) {
        if (data instanceof ModelDataMap map) {
            return ((ModelDataMapAccessor) map).getBackingMap().keySet();
        }
        return Set.of();
    }

    static <T> void copyModelProperty(ModelDataMap.Builder to, IModelData from, ModelProperty<T> property) {
        to.withInitial(property, from.getData(property));
    }
}
