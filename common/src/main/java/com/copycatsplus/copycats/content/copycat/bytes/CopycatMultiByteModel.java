package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock.*;

public class CopycatMultiByteModel implements SimpleMultiStateCopycatPart {

    private static final Map<String, CopycatByteBlock.Byte> byteMap = allBytes.stream()
            .collect(Collectors.toMap(s -> CopycatByteBlock.byByte(s).getName(), s -> s));

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        CopycatByteBlock.Byte bite = byteMap.get(key);
        if (!state.getValue(CopycatByteBlock.byByte(bite))) return;

        int offsetX = bite.x() ? 8 : 0;
        int offsetY = bite.y() ? 8 : 0;
        int offsetZ = bite.z() ? 8 : 0;

        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX, offsetY, offsetZ),
                aabb(4, 4, 4),
                cull(UP | EAST | SOUTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX + 4, offsetY, offsetZ),
                aabb(4, 4, 4).move(12, 0, 0),
                cull(UP | WEST | SOUTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX, offsetY, offsetZ + 4),
                aabb(4, 4, 4).move(0, 0, 12),
                cull(UP | EAST | NORTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX + 4, offsetY, offsetZ + 4),
                aabb(4, 4, 4).move(12, 0, 12),
                cull(UP | WEST | NORTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX, offsetY + 4, offsetZ),
                aabb(4, 4, 4).move(0, 12, 0),
                cull(DOWN | EAST | SOUTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX + 4, offsetY + 4, offsetZ),
                aabb(4, 4, 4).move(12, 12, 0),
                cull(DOWN | WEST | SOUTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX, offsetY + 4, offsetZ + 4),
                aabb(4, 4, 4).move(0, 12, 12),
                cull(DOWN | EAST | NORTH)
        );
        assemblePiece(
                context,
                GlobalTransform.IDENTITY,
                vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                aabb(4, 4, 4).move(12, 12, 12),
                cull(DOWN | WEST | NORTH)
        );
    }

}
