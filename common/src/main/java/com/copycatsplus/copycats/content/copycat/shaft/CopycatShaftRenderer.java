package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.functional.IFunctionalCopycatBlockRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatShaftRenderer extends BracketedKineticBlockEntityRenderer implements IFunctionalCopycatBlockRenderer {
    public CopycatShaftRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(BracketedKineticBlockEntity be, BlockState state) {
        return IFunctionalCopycatBlockRenderer.super.getRotatedModel((IFunctionalCopycatBlockEntity) be, state);
    }
}
