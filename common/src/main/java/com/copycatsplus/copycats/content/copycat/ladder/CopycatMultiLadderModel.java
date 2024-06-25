package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.RAILS;
import static com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock.STEPS;

public class CopycatMultiLadderModel implements SimpleMultiStateCopycatPart {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        if (Objects.equals(key, RAILS.getName()) && !state.getValue(RAILS))
            return;
        if (Objects.equals(key, STEPS.getName()) && !state.getValue(STEPS))
            return;

        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        GlobalTransform transform = t -> t.rotateY(rot);
        if (state.getValue(RAILS)) {
            //Poles
            assemblePiece(context,
                    transform,
                    vec3(2, 0, 0),
                    aabb(2, 16, 1),
                    cull(0));
            assemblePiece(context,
                    transform,
                    vec3(12, 0, 0),
                    aabb(2, 16, 1).move(14, 0, 0),
                    cull(0));
        }

        if (state.getValue(STEPS)) {
            //Steps
            assemblePiece(context,
                    transform,
                    vec3(1, 1, 0.1),
                    aabb(14, 2, 0.8),
                    cull(0));
            assemblePiece(context,
                    transform,
                    vec3(1, 5, 0.1),
                    aabb(14, 2, 0.8),
                    cull(0));
            assemblePiece(context,
                    transform,
                    vec3(1, 9, 0.1),
                    aabb(14, 2, 0.8),
                    cull(0));
            assemblePiece(context,
                    transform,
                    vec3(1, 13, 0.1),
                    aabb(14, 2, 0.8),
                    cull(0));
        }
    }
}
