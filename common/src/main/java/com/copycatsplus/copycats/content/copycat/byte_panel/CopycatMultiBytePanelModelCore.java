package com.copycatsplus.copycats.content.copycat.byte_panel;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.content.copycat.byte_panel.CopycatBytePanelBlock.*;
import static com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock.allBytes;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.utility.MathUtils.replaceAxis;

public class CopycatMultiBytePanelModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_BYTE_PANEL.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (!state.getValue(fromProperty(key))) return;

        int i = key.equals(BOTTOM_LEFT.getName()) || key.equals(TOP_LEFT.getName()) ? 1 : 0;
        int j = key.equals(TOP_LEFT.getName()) || key.equals(TOP_RIGHT.getName()) ? 1 : 0;
        Direction facing = state.getValue(FACING);

        if (facing.getAxis().isHorizontal()) {
            int rot = (int) facing.toYRot();
            AssemblyTransform transform = t -> t.rotateY(rot);

            QuadAutoCull autoCull = autoCull(aabb(8, 8, 16).move(i * 8, j * 8, 0));

            context.assemblePiece(transform,
                    vec3(i * 8, j * 8, 13),
                    aabb(4, 4, 2),
                    cull(UP | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8 + 4, j * 8, 13),
                    aabb(4, 4, 2).move(12, 0, 0),
                    cull(UP | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8, j * 8 + 4, 13),
                    aabb(4, 4, 2).move(0, 12, 0),
                    cull(DOWN | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8 + 4, j * 8 + 4, 13),
                    aabb(4, 4, 2).move(12, 12, 0),
                    cull(DOWN | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8, j * 8, 15),
                    aabb(4, 4, 1).move(0, 0, 15),
                    cull(UP | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8 + 4, j * 8, 15),
                    aabb(4, 4, 1).move(12, 0, 15),
                    cull(UP | WEST | NORTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8, j * 8 + 4, 15),
                    aabb(4, 4, 1).move(0, 12, 15),
                    cull(DOWN | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(transform,
                    vec3(i * 8 + 4, j * 8 + 4, 15),
                    aabb(4, 4, 1).move(12, 12, 15),
                    cull(DOWN | WEST | NORTH),
                    autoCull
            );
        } else if (facing == Direction.DOWN) {
            QuadAutoCull autoCull = autoCull(aabb(8, 16, 8).move(i * 8, 0, j * 8));

            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 0, j * 8),
                    aabb(4, 2, 4),
                    cull(UP | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 0, j * 8),
                    aabb(4, 2, 4).move(12, 0, 0),
                    cull(UP | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 0, j * 8 + 4),
                    aabb(4, 2, 4).move(0, 0, 12),
                    cull(UP | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 0, j * 8 + 4),
                    aabb(4, 2, 4).move(12, 0, 12),
                    cull(UP | WEST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 2, j * 8),
                    aabb(4, 1, 4),
                    cull(DOWN | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 2, j * 8),
                    aabb(4, 1, 4).move(12, 0, 0),
                    cull(DOWN | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 2, j * 8 + 4),
                    aabb(4, 1, 4).move(0, 0, 12),
                    cull(DOWN | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 2, j * 8 + 4),
                    aabb(4, 1, 4).move(12, 0, 12),
                    cull(DOWN | WEST | NORTH),
                    autoCull
            );
        } else if (facing == Direction.UP) {
            QuadAutoCull autoCull = autoCull(aabb(8, 16, 8).move(i * 8, 0, 8 - j * 8));

            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 13, 8 - j * 8),
                    aabb(4, 2, 4),
                    cull(UP | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 13, 8 - j * 8),
                    aabb(4, 2, 4).move(12, 0, 0),
                    cull(UP | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 13, 8 - j * 8 + 4),
                    aabb(4, 2, 4).move(0, 0, 12),
                    cull(UP | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 13, 8 - j * 8 + 4),
                    aabb(4, 2, 4).move(12, 0, 12),
                    cull(UP | WEST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 15, 8 - j * 8),
                    aabb(4, 1, 4),
                    cull(DOWN | EAST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 15, 8 - j * 8),
                    aabb(4, 1, 4).move(12, 0, 0),
                    cull(DOWN | WEST | SOUTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8, 15, 8 - j * 8 + 4),
                    aabb(4, 1, 4).move(0, 0, 12),
                    cull(DOWN | EAST | NORTH),
                    autoCull
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(i * 8 + 4, 15, 8 - j * 8 + 4),
                    aabb(4, 1, 4).move(12, 0, 12),
                    cull(DOWN | WEST | NORTH),
                    autoCull
            );
        }
    }

}
