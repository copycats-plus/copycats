package com.copycatsplus.copycats.content.copycat.base.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;

public interface SimpleCopycatPart {

    static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    @ExpectPlatform
    static BakedModel create(BakedModel original, SimpleCopycatPart part) {
        throw new AssertionError();
    }


    default void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        throw new AssertionError("If this is appearing then a model isn't implemented correctly");
    }

    default MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    default MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x, y, z);
    }

    default MutableVec3 pivot(double x, double y, double z) {
        return new MutableVec3(x, y, z);
    }

    default MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX, sizeY, sizeZ);
    }

    default MutableRotation rot(MutableVec3 pivot, MutableVec3 rot) {
        return new MutableRotation(pivot, rot);
    }

}
