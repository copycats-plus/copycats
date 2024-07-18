package com.copycatsplus.copycats;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelModelCore;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatLargeCogWheelModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.ICopycatPartialModel;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * An enum containing all {@link ICopycatPartialModel}s.
 */
@Environment(EnvType.CLIENT)
public enum CCCopycatPartialModels implements ICopycatPartialModel {
    SHAFT(new CopycatShaftModelCore(), BlockStateProperties.AXIS),
    COGWHEEL(new CopycatCogWheelModelCore(), BlockStateProperties.AXIS),
    LARGE_COGWHEEL(new CopycatLargeCogWheelModelCore(), BlockStateProperties.AXIS);

    /**
     * Creates a new partial model with the given core and block state properties.
     * <p>
     * To ensure correct caching, the model core must assemble the model using only information listed in the blockStateProperties array.
     * It must also render with only the single material block state recorded by {@link KineticCopycatRenderData}.
     * In other words, multi-state rendering is not allowed in a single copycat partial model, but it is possible to
     * render multiple partial models with different materials in a single multi-state copycat.
     * <p>
     * Note that copycat partial models have no block state files, so a SUPER model entry in the {@link CopycatModelCore} will be empty.
     *
     * @param core                 The core of the model.
     * @param blockStateProperties The block state properties used to assemble the model.
     */
    CCCopycatPartialModels(CopycatModelCore core, Property<?>... blockStateProperties) {
        this.model = ICopycatPartialModel.modelOf(core);
        this.properties = blockStateProperties;
    }

    private final BakedModel model;
    private final Property<?>[] properties;

    @Environment(EnvType.CLIENT)
    @Override
    public BakedModel getModel() {
        return model;
    }

    @Override
    public Property<?>[] getProperties() {
        return properties;
    }
}
