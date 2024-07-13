package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatPressurePlateModelCore extends CopycatModelCore {


    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        boolean powered = state.getOptionalValue(PressurePlateBlock.POWERED)
                .or(() -> state.getOptionalValue(WeightedPressurePlateBlock.POWER).map(power -> power > 0))
                .orElse(false);
        if (powered) {
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(1, 0, 1),
                    aabb(7, 0.5f, 7).move(0, 0, 0),
                    cull(SOUTH | EAST)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(8, 0, 8),
                    aabb(7, 0.5f, 7).move(9, 0, 9),
                    cull(NORTH | WEST)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(8, 0, 1),
                    aabb(7, 0.5f, 7).move(9, 0, 0),
                    cull(WEST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(1, 0, 8),
                    aabb(7, 0.5f, 7).move(0, 0, 9),
                    cull(NORTH | EAST)
            );
        } else {
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(1, 0, 1),
                    aabb(7, 1, 7).move(0, 0, 0),
                    cull(SOUTH | EAST)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(8, 0, 8),
                    aabb(7, 1, 7).move(9, 0, 9),
                    cull(NORTH | WEST)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(8, 0, 1),
                    aabb(7, 1, 7).move(9, 0, 0),
                    cull(WEST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(1, 0, 8),
                    aabb(7, 1, 7).move(0, 0, 9),
                    cull(NORTH | EAST)
            );
        }
    }
}
