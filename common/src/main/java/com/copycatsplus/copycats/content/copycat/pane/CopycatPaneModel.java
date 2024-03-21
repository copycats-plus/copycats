package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatPaneModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        List<Direction> present = new ArrayList<>();
        for (Direction direction : Iterate.horizontalDirections) {
            if (state.getValue(CopycatPaneBlock.propertyForDirection(direction)))
                present.add(direction);
        }
        assemblePiece(context, 0, false,
                vec3(7, 0, 7),
                aabb(1, 16, 2).move(7, 0, 7),
                cull(EAST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));
        assemblePiece(context, 0, false,
                vec3(8, 0, 7),
                aabb(1, 16, 2).move(8, 0, 7),
                cull(WEST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));

        for (Direction direction : Iterate.horizontalDirections) {
            if (state.getValue(CopycatPaneBlock.propertyForDirection(direction))) {
                int rot = (int) direction.toYRot();
                assemblePiece(context, rot, false,
                        vec3(7, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0));
                assemblePiece(context, rot, false,
                        vec3(8, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0));
            }
        }

/*        assemblePiece(context, 0, false,
                vec3(7, 0, 7),
                aabb(1, 16, 2).move(7, 0, 7),
                cull(EAST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));
        assemblePiece(context, 0, false,
                vec3(8, 0, 7),
                aabb(1, 16, 2).move(8, 0, 7),
                cull(WEST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));
        if (!present.isEmpty()) {
            for (Direction direction : present) {
                switch (direction) {
                    case NORTH -> {
                        assemblePiece(context, 0, false,
                                vec3(7, 0, 0),
                                aabb(1, 16, 7),
                                cull(EAST | SOUTH));
                        assemblePiece(context, 0, false,
                                vec3(8, 0, 0),
                                aabb(1, 16, 7),
                                cull(WEST | SOUTH));
                    }
                    case EAST -> {
                        assemblePiece(context, 0, false,
                                vec3(9, 0, 7),
                                aabb(7, 16, 1).move(9, 0, 0),
                                cull(WEST | SOUTH));
                        assemblePiece(context, 0, false,
                                vec3(9, 0, 8),
                                aabb(7, 16, 1).move(9, 0, 0),
                                cull(WEST | NORTH));
                    }
                    case SOUTH -> {
                        assemblePiece(context, 0, false,
                                vec3(7, 0, 9),
                                aabb(1, 16, 7).move(0, 0, 9),
                                cull(EAST | NORTH));
                        assemblePiece(context, 0, false,
                                vec3(8, 0, 9),
                                aabb(1, 16, 7).move(0, 0, 9),
                                cull(WEST | NORTH));
                    }
                    case WEST -> {
                        assemblePiece(context, 0, false,
                                vec3(0, 0, 7),
                                aabb(7, 16, 1),
                                cull(EAST | SOUTH));
                        assemblePiece(context, 0, false,
                                vec3(0, 0, 8),
                                aabb(7, 16, 1),
                                cull(EAST | NORTH));
                    }
                    default -> throw new IllegalStateException("Pane cant connect in a non horizontal direction");
                }
            }
        }*/
    }
}
