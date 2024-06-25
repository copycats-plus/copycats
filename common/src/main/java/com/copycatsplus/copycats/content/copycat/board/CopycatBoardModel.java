package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock.byDirection;

public class CopycatBoardModel implements SimpleCopycatPart {

    private static int i(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Map<Direction, Boolean> sides = new HashMap<>();
        for (Direction direction : Iterate.directions) {
            sides.put(direction, state.getValue(byDirection(direction)));
        }

        for (Direction direction : Iterate.directions) {
            if (!sides.get(direction))
                continue;
            if (direction.getAxis().isVertical()) {
                assemblePiece(context,
                        t -> t.flipY(direction == Direction.UP),
                        vec3(0, 0, 0),
                        aabb(16, 1, 16),
                        cull((NORTH * i(sides.get(Direction.NORTH))) |
                                (SOUTH * i(sides.get(Direction.SOUTH))) |
                                (EAST * i(sides.get(Direction.EAST))) |
                                (WEST * i(sides.get(Direction.WEST)))
                        )
                );
            } else {
                Direction right = direction.getClockWise();
                Direction left = direction.getCounterClockWise();
                assemblePiece(context,
                        t -> t.rotateY((int) direction.toYRot() + 180),
                        vec3(0, 0, 0),
                        aabb(16, 16, 1),
                        cull((UP * i(sides.get(Direction.UP))) |
                                (DOWN * i(sides.get(Direction.DOWN))) |
                                (EAST * i(sides.get(right))) |
                                (WEST * i(sides.get(left)))
                        )
                );
            }
        }
    }
}
