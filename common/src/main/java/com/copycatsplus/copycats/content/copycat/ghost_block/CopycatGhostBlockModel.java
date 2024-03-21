package com.copycatsplus.copycats.content.copycat.ghost_block;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;

public class CopycatGhostBlockModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        assembleQuad(context); // assemble without any modifications
    }
}
