package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock.*;

public class CopycatMultiByteModelCore extends CopycatModelCore {

    private static final Map<String, CopycatByteBlock.Byte> byteMap = allBytes.stream()
            .collect(Collectors.toMap(s -> CopycatByteBlock.byByte(s).getName(), s -> s));

    @Override
    public void registerModels(List<ModelEntry> entries) {
        registerForMultiState(entries, CCBlocks.COPYCAT_BYTE.get(), false);
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        CopycatByteBlock.Byte bite = byteMap.get(key);
        if (!state.getValue(CopycatByteBlock.byByte(bite))) return;

        int offsetX = bite.x() ? 8 : 0;
        int offsetY = bite.y() ? 8 : 0;
        int offsetZ = bite.z() ? 8 : 0;

        QuadAutoCull autoCull = autoCull(aabb(8, 8, 8).move(offsetX, offsetY, offsetZ));

        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX, offsetY, offsetZ),
                aabb(4, 4, 4),
                cull(UP | EAST | SOUTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX + 4, offsetY, offsetZ),
                aabb(4, 4, 4).move(12, 0, 0),
                cull(UP | WEST | SOUTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX, offsetY, offsetZ + 4),
                aabb(4, 4, 4).move(0, 0, 12),
                cull(UP | EAST | NORTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX + 4, offsetY, offsetZ + 4),
                aabb(4, 4, 4).move(12, 0, 12),
                cull(UP | WEST | NORTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX, offsetY + 4, offsetZ),
                aabb(4, 4, 4).move(0, 12, 0),
                cull(DOWN | EAST | SOUTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX + 4, offsetY + 4, offsetZ),
                aabb(4, 4, 4).move(12, 12, 0),
                cull(DOWN | WEST | SOUTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX, offsetY + 4, offsetZ + 4),
                aabb(4, 4, 4).move(0, 12, 12),
                cull(DOWN | EAST | NORTH),
                autoCull
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                aabb(4, 4, 4).move(12, 12, 12),
                cull(DOWN | WEST | NORTH),
                autoCull
        );
    }

}
