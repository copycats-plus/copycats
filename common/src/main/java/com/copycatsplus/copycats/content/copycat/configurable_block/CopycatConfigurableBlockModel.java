package com.copycatsplus.copycats.content.copycat.configurable_block;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatConfigurableBlockModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        if (state.getBlock() instanceof CopycatConfigurableBlock configurable) {
            Vec3 offset = configurable.getOffset();
            Vec3 size = configurable.getSize();
            double sizeX = size.x();
            double sizeY = size.y();
            double sizeZ = size.z();
            double halfX = sizeX /2;
            double halfY = sizeY /2;
            double halfZ= sizeZ /2;
            assemblePiece(context, 0, false,
                    vec3(offset.x() + halfX, offset.y(), offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 0, 16 - halfZ),
                    cull(NORTH | WEST | UP));
            assemblePiece(context, 0, false,
                    vec3(offset.x(), offset.y(), offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(0, 0, 16 - halfZ),
                    cull(NORTH | EAST | UP));
            assemblePiece(context, 0, false,
                    vec3(offset.x(), offset.y(), offset.z()),
                    aabb(halfX, halfY, halfZ).move(0, 0, 0),
                    cull(SOUTH | EAST | UP));
            assemblePiece(context, 0, false,
                    vec3(offset.x() + halfX, offset.y(), offset.z()),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 0, 0),
                    cull(SOUTH | WEST | UP));
            //Top
            assemblePiece(context, 0, false,
                    vec3(offset.x() + halfX, offset.y() + halfY, offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 16 - halfY, 16 - halfZ),
                    cull(NORTH | WEST | DOWN));
            assemblePiece(context, 0, false,
                    vec3(offset.x(), offset.y() + halfY, offset.z() + halfZ),
                    aabb(halfX, halfY, halfZ).move(0, 16 - halfY, 16 - halfZ),
                    cull(NORTH | EAST | DOWN));
            assemblePiece(context, 0, false,
                    vec3(offset.x(), offset.y() + halfY, offset.z()),
                    aabb(halfX, halfY, halfZ).move(0, 16 - halfY, 0),
                    cull(SOUTH | EAST | DOWN));
            assemblePiece(context, 0, false,
                    vec3(offset.x() + halfX, offset.y() + halfY, offset.z()),
                    aabb(halfX, halfY, halfZ).move(16 - halfX, 16 - halfY, 0),
                    cull(SOUTH | WEST | DOWN));
        }
    }
}
