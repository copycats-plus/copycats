package com.copycatsplus.copycats.content.copycat.configurable_block;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatConfigurableBlockModel extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (state.getBlock() instanceof CopycatConfigurableBlock configurable) {

            byte[] packedOffsets = CopycatConfigurableBlockBlockEntity.unpackOffsets(state.getValue(CopycatConfigurableBlock.SOLID_FACES));

            float northOffset = packedOffsets[Direction.NORTH.ordinal()];
            float southOffset = packedOffsets[Direction.SOUTH.ordinal()];
            float eastOffset = packedOffsets[Direction.EAST.ordinal()];
            float westOffset = packedOffsets[Direction.WEST.ordinal()];
            float upOffset = packedOffsets[Direction.UP.ordinal()];
            float downOffset = packedOffsets[Direction.DOWN.ordinal()];

            Direction.stream().forEach(dir -> Copycats.LOGGER.info("Dir: {} Offset: {}", dir, packedOffsets[dir.ordinal()]));

            //NORTH-WEST DOWN
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0, 16 - downOffset - 1, 0),
                    aabb(8, 8, 8),
                    cull(UP | EAST | SOUTH)
            );

            //NORTH-EAST DOWN
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0 + 8, 16 - downOffset - 1, 0),
                    aabb(8, 8, 8).move(8, 0, 0),
                    cull(UP | WEST | SOUTH)
            );

            //SOUTH-WEST DOWN
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0, 16 - downOffset - 1, 0 + 8),
                    aabb(8, 8, 8).move(0, 0, 8),
                    cull(UP | EAST | NORTH)
            );

            //SOUTH-EAST DOWN
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0 + 8, 16 - downOffset - 1, 0 + 8),
                    aabb(8, 8, 8).move(8, 0, 8),
                    cull(UP | WEST | NORTH)
            );

            //NORTH-WEST UP
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0, 0 + 8, 0),
                    aabb(8, 8, 8).move(0, 8, 0),
                    cull(DOWN | EAST | SOUTH)
            );

            //NORTH-EAST UP
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0 + 8, 0 + 8, 0),
                    aabb(8, 8, 8).move(8, 8, 0),
                    cull(DOWN | WEST | SOUTH)
            );

            //SOUTH-WEST UP
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0, 0 + 8, 0 + 8),
                    aabb(8, 8, 8).move(0, 8, 8),
                    cull(DOWN | EAST | NORTH)
            );

            //SOUTH-EAST UP
            context.assemblePiece(
                    AssemblyTransform.IDENTITY,
                    vec3(0 + 8, 0 + 8, 0 + 8),
                    aabb(8, 8, 8).move(8, 8, 8),
                    cull(DOWN | WEST | NORTH)
            );


           /* Vec3 offset = configurable.getOffset();
            Vec3 size = configurable.getSize();
            double sizeX = size.x();
            double sizeY = size.y();
            double sizeZ = size.z();
            double halfX = sizeX /2;
            double halfY = sizeY /2;
            double halfZ= sizeZ /2;
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x() + halfX, offset.y(), offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 0, 16 - halfZ),
                    cull(NORTH | WEST | UP));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x(), offset.y(), offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(0, 0, 16 - halfZ),
                    cull(NORTH | EAST | UP));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x(), offset.y(), offset.z()),
                    aabb(halfX, halfY, halfZ).move(0, 0, 0),
                    cull(SOUTH | EAST | UP));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x() + halfX, offset.y(), offset.z()),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 0, 0),
                    cull(SOUTH | WEST | UP));
            //Top
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x() + halfX, offset.y() + halfY, offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 16 - halfY, 16 - halfZ),
                    cull(NORTH | WEST | DOWN));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x(), offset.y() + halfY, offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(0, 16 - halfY, 16 - halfZ),
                    cull(NORTH | EAST | DOWN));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x(), offset.y() + halfY, offset.z()),
                    aabb(halfX, halfY, halfZ).move(0, 16 - halfY, 0),
                    cull(SOUTH | EAST | DOWN));
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(offset.x() + halfX, offset.y() + halfY, offset.z()),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 16 - halfY, 0),
                    cull(SOUTH | WEST | DOWN));*/
        }
    }
}
