package com.copycatsplus.copycats.content.copycat.fence_gate;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static net.minecraft.world.level.block.FenceGateBlock.*;

public class CopycatFenceGateModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(FenceGateBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof FenceGateBlock) {
            context.assembleAll();
            return;
        }

        int offsetWall = state.getValue(IN_WALL) ? -3 : 0;
        int rot = (int) state.getValue(FACING).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);

        // Assemble the poles
        for (boolean eastSide : Iterate.falseAndTrue) {
            int offsetX = eastSide ? 14 : 0;
            context.assemblePiece(
                    transform,
                    vec3(offsetX, 5 + offsetWall, 7),
                    aabb(1, 6, 1),
                    cull(UP | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX + 1, 5 + offsetWall, 7),
                    aabb(1, 6, 1).move(15, 0, 0),
                    cull(UP | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX, 5 + offsetWall, 8),
                    aabb(1, 6, 1).move(0, 0, 15),
                    cull(UP | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX + 1, 5 + offsetWall, 8),
                    aabb(1, 6, 1).move(15, 0, 15),
                    cull(UP | NORTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX, 11 + offsetWall, 7),
                    aabb(1, 5, 1).move(0, 11, 0),
                    cull(DOWN | SOUTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX + 1, 11 + offsetWall, 7),
                    aabb(1, 5, 1).move(15, 11, 0),
                    cull(DOWN | SOUTH | WEST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX, 11 + offsetWall, 8),
                    aabb(1, 5, 1).move(0, 11, 15),
                    cull(DOWN | NORTH | EAST)
            );
            context.assemblePiece(
                    transform,
                    vec3(offsetX + 1, 11 + offsetWall, 8),
                    aabb(1, 5, 1).move(15, 11, 15),
                    cull(DOWN | NORTH | WEST)
            );
        }

        if (state.getValue(OPEN)) {
            for (boolean eastDoor : Iterate.falseAndTrue) {
                for (boolean eastSide : Iterate.falseAndTrue) {
                    int offsetX = (eastDoor ? 14 : 0) + (eastSide ? 1 : 0);
                    context.assemblePiece(
                            transform,
                            vec3(offsetX, 12 + offsetWall, 9),
                            aabb(1, 3, 6).move(eastSide ? 15 : 0, 13, 10),
                            cull(NORTH | (eastSide ? WEST : EAST))
                    );
                    context.assemblePiece(
                            transform,
                            vec3(offsetX, 9 + offsetWall, 13),
                            aabb(1, 3, 2).move(eastSide ? 15 : 0, 7, 14),
                            cull(UP | DOWN | (eastSide ? WEST : EAST))
                    );
                    context.assemblePiece(
                            transform,
                            vec3(offsetX, 6 + offsetWall, 9),
                            aabb(1, 3, 6).move(eastSide ? 15 : 0, 0, 10),
                            cull(NORTH | (eastSide ? WEST : EAST))
                    );
                }
            }
        } else {
            for (boolean southSide : Iterate.falseAndTrue) {
                int rot2 = rot + (southSide ? 180 : 0);
                AssemblyTransform transform2 = t -> t.rotateY(rot2);
                context.assemblePiece(
                        transform2,
                        vec3(8, 12 + offsetWall, 7),
                        aabb(6, 3, 1).move(0, 13, 0),
                        cull(SOUTH | EAST | WEST)
                );
                context.assemblePiece(
                        transform2,
                        vec3(8, 9 + offsetWall, 7),
                        aabb(2, 3, 1).move(0, 7, 0),
                        cull(UP | DOWN | SOUTH | WEST)
                );
                context.assemblePiece(
                        transform2,
                        vec3(8, 6 + offsetWall, 7),
                        aabb(6, 3, 1),
                        cull(SOUTH | EAST | WEST)
                );
                context.assemblePiece(
                        transform2,
                        vec3(2, 12 + offsetWall, 7),
                        aabb(6, 3, 1).move(10, 13, 0),
                        cull(SOUTH | EAST | WEST)
                );
                context.assemblePiece(
                        transform2,
                        vec3(6, 9 + offsetWall, 7),
                        aabb(2, 3, 1).move(14, 7, 0),
                        cull(UP | DOWN | SOUTH | EAST)
                );
                context.assemblePiece(
                        transform2,
                        vec3(2, 6 + offsetWall, 7),
                        aabb(6, 3, 1).move(10, 0, 0),
                        cull(SOUTH | EAST | WEST)
                );
            }
        }
    }

}
