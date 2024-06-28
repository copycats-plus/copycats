package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.world.level.block.state.BlockState;

public interface IFunctionalCopycatBlockRenderer {

    default SuperByteBuffer getRotatedModel(IFunctionalCopycatBlockEntity be, BlockState state) {
        return FunctionalCopycatRenderHelper.getBuffer(be);
    }
}
