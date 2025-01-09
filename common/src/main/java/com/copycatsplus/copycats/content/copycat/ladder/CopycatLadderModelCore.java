package com.copycatsplus.copycats.content.copycat.ladder;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;

public class CopycatLadderModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(LadderBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof LadderBlock) {
            context.assembleAll();
            return;
        }

        int rot = (int) state.getValue(LadderBlock.FACING).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        assemblePoles(context, transform);
        assembleSteps(context, transform);
    }

    public static void assemblePoles(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(
                transform,
                vec3(2, 0, 0),
                aabb(2, 16, 1),
                cull(0)
        );
        context.assemblePiece(
                transform,
                vec3(12, 0, 0),
                aabb(2, 16, 1).move(14, 0, 0),
                cull(0)
        );
    }

    public static void assembleSteps(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(
                transform,
                vec3(1, 1, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        context.assemblePiece(
                transform,
                vec3(1, 5, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        context.assemblePiece(
                transform,
                vec3(1, 9, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
        context.assemblePiece(
                transform,
                vec3(1, 13, 0.1),
                aabb(14, 2, 0.8),
                cull(0)
        );
    }
}
