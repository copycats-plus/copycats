package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.IKineticCopycatBlockInstance;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityInstance;

public class CopycatShaftInstance extends BracketedKineticBlockEntityInstance implements IKineticCopycatBlockInstance {
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
    public ICopycatBlockEntity getBlockEntity() {
        return (ICopycatBlockEntity) blockEntity;
    }

    @Override
    public Material<RotatingData> getRotatingMaterial() {
        return IKineticCopycatBlockInstance.super.getRotatingMaterial();
    }

    @Override
    public Instancer<RotatingData> getModel() {
        return IKineticCopycatBlockInstance.super.getModel(CCCopycatPartialModels.SHAFT);
    }

    @Override
    public boolean shouldReset() {
        return IKineticCopycatBlockInstance.super.shouldReset();
    }
}
