package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.cull;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatSlidingDoorModelCore extends CopycatModelCore {

    private final boolean kinetic;

    public CopycatSlidingDoorModelCore(boolean kinetic) {
        this.kinetic = kinetic;
    }

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(DoorBlock.class), kinetic ? EntryType.KINETIC_COPYCAT : EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!kinetic && !state.getValue(SlidingDoorBlock.VISIBLE)) {
            return;
        }

        if (material.getBlock() instanceof DoorBlock) {
            context.assembleAll();
            return;
        }

        if (state.getValue(CopycatSlidingDoorBlock.CT)) {
            assembleWithCT(state, context);
        } else {
            assembleWithoutCT(state, context);
        }
    }

    private void assembleWithCT(BlockState state, CopycatRenderContext context) {
        int rot = (int) state.getValue(DoorBlock.FACING).toYRot();
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 12, 2),
                    cull(SOUTH | UP));
            context.assemblePiece(transform,
                    vec3(0, 12, 0),
                    aabb(16, 4, 2).move(0, 4, 0),
                    cull(SOUTH | DOWN | (kinetic ? UP : 0)));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 12, 1).move(0, 0, 15),
                    cull(NORTH | UP));
            context.assemblePiece(transform,
                    vec3(0, 12, 2),
                    aabb(16, 4, 1).move(0, 4, 15),
                    cull(NORTH | DOWN | (kinetic ? UP : 0)));
        } else {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 4, 0),
                    aabb(16, 12, 2).move(0, 4, 0),
                    cull(SOUTH | DOWN));
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 4, 2).move(0, 8, 0),
                    cull(SOUTH | UP | (kinetic ? DOWN : 0)));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 4, 2),
                    aabb(16, 12, 1).move(0, 4, 15),
                    cull(NORTH | DOWN));
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 4, 1).move(0, 8, 15),
                    cull(NORTH | UP | (kinetic ? DOWN : 0)));
        }
    }

    private void assembleWithoutCT(BlockState state, CopycatRenderContext context) {
        int rot = (int) state.getValue(DoorBlock.FACING).toYRot();
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (half == DoubleBlockHalf.LOWER) {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 2),
                    cull(SOUTH | (kinetic ? UP : 0)));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 16, 1).move(0, 0, 15),
                    cull(NORTH | (kinetic ? UP : 0)));
        } else {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 2).move(0, 0, 0),
                    cull(SOUTH | (kinetic ? DOWN : 0)));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 16, 1).move(0, 0, 15),
                    cull(NORTH | (kinetic ? DOWN : 0)));
        }
    }
}
