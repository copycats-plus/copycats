package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class CopycatSlabModel extends SimpleCopycatModel {

    protected static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    public CopycatSlabModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        Direction facing = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).isPresent() ? CopycatSlabBlock.getApparentDirection(state) : Direction.UP;
        boolean isDouble = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).orElse(SlabType.BOTTOM) == SlabType.DOUBLE;

        // 2 pieces
        for (boolean front : Iterate.trueAndFalse) {
            assemblePiece(facing, context, front, false, isDouble);
        }

        // 2 more pieces for double slabs
        if (isDouble) {
            for (boolean front : Iterate.trueAndFalse) {
                assemblePiece(facing, context, front, true, isDouble);
            }
        }
    }

    private void assemblePiece(Direction facing, CopycatRenderContext context, boolean front, boolean topSlab, boolean isDouble) {
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled12 = normal.scale(12 / 16f);
        Vec3 normalScaledN8 = topSlab ? normal.scale((front ? 0 : -8) / 16f) : normal.scale((front ? 8 : 0) / 16f);
        float contract = 12;
        AABB bb = CUBE_AABB.contract(normal.x * contract / 16, normal.y * contract / 16, normal.z * contract / 16);
        if (!front)
            bb = bb.move(normalScaled12);

        Direction direction = context.src().lightFace();

        if (front && direction == facing)
            return;
        if (!front && direction == facing.getOpposite())
            return;
        if (isDouble && topSlab && direction == facing)
            return;
        if (isDouble && !topSlab && direction == facing.getOpposite())
            return;

        assembleQuad(context, bb, normalScaledN8);
    }
}
