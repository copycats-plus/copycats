package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatLadderModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        //Poles
        assemblePiece(context, rot, false,
                vec3(2, 0, 0),
                aabb(2, 16, 1),
                cull(0));
        assemblePiece(context, rot, false,
                vec3(12, 0, 0),
                aabb(2, 16, 1).move(14, 0, 0),
                cull(0));

        //Steps
        assemblePiece(context, rot, false,
                vec3(1, 1, 0.1),
                aabb(14, 2, 0.8),
                cull(0));
        assemblePiece(context, rot, false,
                vec3(1, 5, 0.1),
                aabb(14, 2, 0.8),
                cull(0));
        assemblePiece(context, rot, false,
                vec3(1, 9, 0.1),
                aabb(14, 2, 0.8),
                cull(0));
        assemblePiece(context, rot, false,
                vec3(1, 13, 0.1),
                aabb(14, 2, 0.8),
                cull(0));
    }
}
