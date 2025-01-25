package com.copycatsplus.copycats.content.copycat.half_stairs;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatHalfStairsMultiModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_HALF_STAIR.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {

        AssemblyTransform transform = t -> t.rotateY(90);
        context.assemblePiece(transform,
                vec3(14.0, 12.0, 4.0),
                aabb(4, 8, 8),
                cull(UP | NORTH | SOUTH | EAST)
        );

        context.assemblePiece(transform,
                vec3(10.0, 12.0, 4.0),
                aabb(4, 8, 8),
                cull(UP | NORTH | SOUTH)
        );

        context.assemblePiece(transform,
                vec3(4.0, 2.0, 4.0),
                aabb(8, 4, 8),
                cull(0)
        );

        context.assemblePiece(transform,
                vec3(4.0, 6.0, 4.0),
                aabb(8, 4, 8),
                cull(0)
        );

        context.assemblePiece(transform,
                vec3(12.0, 4.0, 4.0),
                aabb(8, 8, 8),
                cull(0)
        );
    }
}
