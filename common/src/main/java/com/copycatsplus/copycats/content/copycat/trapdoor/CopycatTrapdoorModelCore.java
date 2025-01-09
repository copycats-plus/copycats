package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(TrapDoorBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof TrapDoorBlock) {
            context.assembleAll();
            return;
        }

        int rot = (int) state.getValue(FACING).toYRot();
        boolean flipY = state.getValue(HALF) == Half.TOP;
        boolean open = state.getValue(OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot).flipY(flipY);
        if (!open) {
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 1, 16),
                    cull(UP)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 1, 0),
                    aabb(16, 2, 16).move(0, 14, 0),
                    cull(DOWN)
            );
        } else {
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 1),
                    cull(SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(0, 0, 1),
                    aabb(16, 16, 2).move(0, 0, 14),
                    cull(NORTH)
            );
        }
    }
}
