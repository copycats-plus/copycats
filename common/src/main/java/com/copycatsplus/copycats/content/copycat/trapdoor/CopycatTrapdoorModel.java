package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        int rot = (int) state.getValue(FACING).toYRot();
        boolean flipY = state.getValue(HALF) == Half.TOP;
        boolean open = state.getValue(OPEN);
        GlobalTransform transform = t -> t.rotateY(rot).flipY(flipY);
        if (!open) {
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 1, 16),
                    cull(UP)
            );
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 1, 0),
                    aabb(16, 2, 16).move(0, 14, 0),
                    cull(DOWN)
            );
        } else {
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 1),
                    cull(SOUTH)
            );
            assemblePiece(
                    context,
                    transform,
                    vec3(0, 0, 1),
                    aabb(16, 16, 2).move(0, 0, 14),
                    cull(NORTH)
            );
        }
    }
}
