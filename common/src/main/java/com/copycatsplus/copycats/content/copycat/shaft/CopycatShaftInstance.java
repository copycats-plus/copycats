package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.functional.IFunctionalCopycatBlockInstance;
import com.copycatsplus.copycats.content.copycat.base.model.functional.KineticCopycatRenderData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityInstance;

public class CopycatShaftInstance extends BracketedKineticBlockEntityInstance implements IFunctionalCopycatBlockInstance {
    protected KineticCopycatRenderData renderData;

    public CopycatShaftInstance(MaterialManager materialManager, BracketedKineticBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    public KineticCopycatRenderData getRenderData() {
        return renderData;
    }

    @Override
    public MaterialManager getMaterialManager() {
        return materialManager;
    }

    @Override
    public void setRenderData(KineticCopycatRenderData renderData) {
        this.renderData = renderData;
    }

    @Override
    public IFunctionalCopycatBlockEntity getBlockEntity() {
        return (IFunctionalCopycatBlockEntity) blockEntity;
    }

    @Override
    public Material<RotatingData> getRotatingMaterial() {
        return IFunctionalCopycatBlockInstance.super.getRotatingMaterial();
    }

    @Override
    public Instancer<RotatingData> getModel() {
        return IFunctionalCopycatBlockInstance.super.getModel();
    }

    @Override
    public boolean shouldReset() {
        return IFunctionalCopycatBlockInstance.super.shouldReset();
    }
}
