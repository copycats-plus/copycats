package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.RAILS;
import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.STEPS;

public class CopycatMultiLadderModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (Objects.equals(key, RAILS.getName()) && !state.getValue(RAILS))
            return;
        if (Objects.equals(key, STEPS.getName()) && !state.getValue(STEPS))
            return;

        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (state.getValue(RAILS)) {
            CopycatLadderModelCore.assemblePoles(context, transform);
        }

        if (state.getValue(STEPS)) {
            CopycatLadderModelCore.assembleSteps(context, transform);
        }
    }
}
