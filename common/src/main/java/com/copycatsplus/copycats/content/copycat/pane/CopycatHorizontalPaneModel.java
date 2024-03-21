package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatHorizontalPaneModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        List<Direction> present = new ArrayList<>();
        //middle
        assemblePiece(context, 0, false,
                vec3(0, 7, 0),
                aabb(16, 1, 16),
                cull(UP));
        assemblePiece(context, 0, false,
                vec3(0, 8, 0),
                aabb(16, 1, 16),
                cull(DOWN));
    }
}
