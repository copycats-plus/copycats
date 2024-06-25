package com.copycatsplus.copycats.content.copycat.test_block;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import com.copycatsplus.copycats.content.copycat.slab.CopycatMultiSlabModel;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatTestBlockModel implements SimpleMultiStateCopycatPart {

    private final CopycatMultiSlabModel model = new CopycatMultiSlabModel();

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        model.emitCopycatQuads(key, state, context, material);
    }
}
