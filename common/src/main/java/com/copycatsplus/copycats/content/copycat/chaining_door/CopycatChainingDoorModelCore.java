package com.copycatsplus.copycats.content.copycat.chaining_door;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatChainingDoorModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {

        if (state.getValue(CopycatChainingDoorBlock.CT)) {
            assembleWithCT(state, context);
        } else {
            assembleWithoutCT(state, context);
        }
    }

    private static void assembleWithCT(BlockState state, CopycatRenderContext context) {
        int rot = (int) state.getValue(CopycatChainingDoorBlock.FACING).toYRot();
        boolean rightHinge = state.getValue(CopycatChainingDoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
        boolean open = state.getValue(CopycatChainingDoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (!open) {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 2),
                    cull(SOUTH));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 16, 1).move(0, 0, 15),
                    cull(NORTH));
        } else {
            if (!rightHinge) {
                transform = t -> t.flipX(true).rotateY(rot);
            }
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(2, 16, 16),
                    cull(EAST));
            //Back
            context.assemblePiece(transform,
                    vec3(2, 0, 0),
                    aabb(1, 16, 16).move(15, 0, 0),
                    cull(WEST));
        }
    }

    private static void assembleWithoutCT(BlockState state, CopycatRenderContext context) {
        int rot = (int) state.getValue(DoorBlock.FACING).toYRot();
        boolean rightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
        DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
        boolean open = state.getValue(DoorBlock.OPEN);
        AssemblyTransform transform = t -> t.rotateY(rot);
        if (!open) {
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, 2),
                    cull(SOUTH));
            //Back
            context.assemblePiece(transform,
                    vec3(0, 0, 2),
                    aabb(16, 16, 1).move(0, 0, 15),
                    cull(NORTH));
        } else {
            if (!rightHinge) {
                transform = t -> t.flipX(true).rotateY(rot);
            }
            //Front
            context.assemblePiece(transform,
                    vec3(0, 0, 0),
                    aabb(2, 16, 16),
                    cull(EAST));
            //Back
            context.assemblePiece(transform,
                    vec3(2, 0, 0),
                    aabb(1, 16, 16).move(15, 0, 0),
                    cull(WEST));
        }
    }
}
