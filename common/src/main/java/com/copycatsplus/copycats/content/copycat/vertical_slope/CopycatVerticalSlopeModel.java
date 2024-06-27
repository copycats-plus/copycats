package com.copycatsplus.copycats.content.copycat.vertical_slope;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.slope.CopycatSlopeModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;

public class CopycatVerticalSlopeModel implements SimpleCopycatPart {

    private final boolean enhanced;

    public CopycatVerticalSlopeModel(boolean enhanced) {
        this.enhanced = enhanced;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getValue(CopycatVerticalSlopeBlock.FACING);
        int rot = (int) facing.toYRot();
        GlobalTransform transform = t -> t.rotateZ(-90).rotateY(rot);
        CopycatSlopeModel.assembleSlope(context, transform, 0, 16, enhanced);
    }
}
