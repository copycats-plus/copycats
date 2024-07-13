package com.copycatsplus.copycats.utility.forge;

import com.jozufozu.flywheel.core.virtual.VirtualEmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

public class ModelUtils {
    public static final ModelProperty<Boolean> VIRTUAL_PROPERTY = new ModelProperty<>();
    public static final IModelData VIRTUAL_DATA = new ModelDataMap.Builder().withInitial(VIRTUAL_PROPERTY, true).build();

    public static boolean isVirtual(IModelData data) {
        return Boolean.TRUE.equals(data.getData(VIRTUAL_PROPERTY)) || VirtualEmptyModelData.is(data);
    }
}
