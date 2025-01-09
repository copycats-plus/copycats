package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatShaftModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(ShaftBlock.class), EntryType.KINETIC_COPYCAT));
        entries.add(new ModelEntry("shaft", ModelGetter.MATERIAL, this, updatePropertiesIfMatch(ShaftBlock.class), EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof ShaftBlock) {
            context.assembleAll();
            return;
        }

        Axis axis = state.getValue(CopycatShaftBlock.AXIS);

        AssemblyTransform transform = t -> t.rotateY(axis == Axis.X ? 90 : 0).rotateX(axis == Axis.Y ? 90 : 0);
        context.assemblePiece(
                transform,
                vec3(6, 6, 0),
                aabb(2, 2, 16).move(0, 0, 0),
                cull(UP | EAST),
                noCull()
        );
        context.assemblePiece(
                transform,
                vec3(8, 6, 0),
                aabb(2, 2, 16).move(14, 0, 0),
                cull(UP | WEST),
                noCull()
        );
        context.assemblePiece(
                transform,
                vec3(6, 8, 0),
                aabb(2, 2, 16).move(0, 14, 0),
                cull(DOWN | EAST),
                noCull()
        );
        context.assemblePiece(
                transform,
                vec3(8, 8, 0),
                aabb(2, 2, 16).move(14, 14, 0),
                cull(DOWN | WEST),
                noCull()
        );
    }
}
