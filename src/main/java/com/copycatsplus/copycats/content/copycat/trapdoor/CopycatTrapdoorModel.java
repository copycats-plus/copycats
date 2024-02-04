package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.content.copycat.SimpleCopycatModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModel extends SimpleCopycatModel {

    public CopycatTrapdoorModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitCopycatQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(FACING).toYRot();
        boolean flipY = state.getValue(HALF) == Half.TOP;
        boolean open = state.getValue(OPEN);

        if (!open) {
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 0),
                    aabb(16, 1, 16),
                    cull(UP)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 1, 0),
                    aabb(16, 2, 16).move(0, 14, 0),
                    cull(DOWN)
            );
        } else {
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 0),
                    aabb(16, 16, 1),
                    cull(SOUTH)
            );
            assemblePiece(
                    context, rot, flipY,
                    vec3(0, 0, 1),
                    aabb(16, 16, 2).move(0, 0, 14),
                    cull(NORTH)
            );
        }
    }
}
