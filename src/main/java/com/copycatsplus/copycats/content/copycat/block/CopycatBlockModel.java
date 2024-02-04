package com.copycatsplus.copycats.content.copycat.block;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatBlockModel extends SimpleCopycatModel {

    public CopycatBlockModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        assembleQuad(context); // assemble without any modifications
    }
}
