package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.utility.ChatUtils;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.backend.RenderLayer;
import com.jozufozu.flywheel.config.BackendType;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

import javax.annotation.Nullable;

public interface IFunctionalCopycatBlockInstance {

    @Nullable
    KineticCopycatRenderData getRenderData();

    MaterialManager getMaterialManager();

    void setRenderData(KineticCopycatRenderData renderData);

    IFunctionalCopycatBlockEntity getBlockEntity();

    default Material<RotatingData> getRotatingMaterial() {
        RenderType type = ItemBlockRenderTypes.getChunkRenderType(getRenderData().material());
        RenderLayer layer = RenderLayer.getLayer(type);
        if (layer == null) layer = RenderLayer.TRANSPARENT;

        // workaround for flywheel crash when transparent layer is used in batching backend
        if (Backend.getBackendType() == BackendType.BATCHING && type == RenderType.translucent()) {
            type = RenderType.cutoutMipped();
            ChatUtils.sendWarningOnce(
                    "flywheel_batching_translucent",
                    "Translucent textures may appear slightly broken when using the Flywheel batching backend. Please switch to the instancing backend instead."
            );
        }

        return getMaterialManager().state(layer, type)
                .material(AllMaterialSpecs.ROTATING);
    }

    default Instancer<RotatingData> getModel() {
        KineticCopycatRenderData renderData = KineticCopycatRenderData.of(getBlockEntity());
        setRenderData(renderData);
        return getRotatingMaterial().model(renderData, () -> FunctionalCopycatRenderHelper.getInstanceModel(getBlockEntity(), renderData));
    }

    default boolean shouldReset() {
        if (getRenderData() == null)
            return true;
        if (getRenderData().enableCT() != getBlockEntity().isCTEnabled())
            return true;
        if (!getRenderData().material().equals(getBlockEntity().getMaterial()))
            return true;
        if (!getRenderData().state().equals(getBlockEntity().getBlockState()))
            return true;
        return false;
    }
}
