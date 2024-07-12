package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatByteModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
            if (!state.getValue(CopycatByteBlock.byByte(bite))) continue;

            int offsetX = bite.x() ? 8 : 0;
            int offsetY = bite.y() ? 8 : 0;
            int offsetZ = bite.z() ? 8 : 0;

            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX, offsetY, offsetZ),
                    aabb(4, 4, 4),
                    cull(UP | EAST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX + 4, offsetY, offsetZ),
                    aabb(4, 4, 4).move(12, 0, 0),
                    cull(UP | WEST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 0, 12),
                    cull(UP | EAST | NORTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX + 4, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 0, 12),
                    cull(UP | WEST | NORTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(0, 12, 0),
                    cull(DOWN | EAST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX + 4, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(12, 12, 0),
                    cull(DOWN | WEST | SOUTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 12, 12),
                    cull(DOWN | EAST | NORTH)
            );
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 12, 12),
                    cull(DOWN | WEST | NORTH)
            );
        }
    }
}
