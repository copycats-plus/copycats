package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatByteModel extends SimpleCopycatModel {

    public CopycatByteModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes){
            if (!state.getValue(CopycatByteBlock.byByte(bite))) continue;

            int offsetX = bite.x() ? 8 : 0;
            int offsetY = bite.y() ? 8 : 0;
            int offsetZ = bite.z() ? 8 : 0;

            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY, offsetZ),
                    aabb(4, 4, 4),
                    cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY, offsetZ),
                    aabb(4, 4, 4).move(12, 0, 0),
                    cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 0, 12),
                    cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 0, 12),
                    cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(0, 12, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY + 4, offsetZ),
                    aabb(4, 4, 4).move(12, 12, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(0, 12, 12),
                    cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH)
            );
            assemblePiece(
                    context, 0, false,
                    vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                    aabb(4, 4, 4).move(12, 12, 12),
                    cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH)
            );
        }
    }
}
