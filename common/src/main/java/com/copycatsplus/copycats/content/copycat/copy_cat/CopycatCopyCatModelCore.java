package com.copycatsplus.copycats.content.copycat.copy_cat;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatCopyCatModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(CopycatCopyCatBlock.FACING).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
        context.assemblePiece(transform,
                vec3(8.5, -3.2, 6.25),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(NORTH | EAST),
                rotate(
                        pivot(16.5, 4.9, 14.25),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.5, 0, 7),
                aabb(1.0, 1, 3),
                cull(0),
                rotate(
                        pivot(16.5, 8, 2),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7, 0, 7),
                aabb(1, 1, 3),
                cull(0),
                rotate(
                        pivot(15, 6, 2),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.250000000000004, 5.999999999999979, 9.449999999999996),
                aabb(1.0, 8.000000000000023, 1.5),
                cull(NORTH | WEST),
                rotate(
                        pivot(-2.000000000000001, 5.9999999999999964, 17.350000000000005),
                        angle(-45, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7, 5.75, 3.5),
                aabb(2.5, 2.0, 2.5),
                cull(0),
                rotate(
                        pivot(15, 13.75, 11.5),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.5, 5.75, 3),
                aabb(1.5, 1.0, 0.5),
                cull(0),
                rotate(
                        pivot(15.5, 13.75, 11),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.25, 7.75, 5),
                aabb(0.5, 0.5, 1),
                cull(0),
                rotate(
                        pivot(15.25, 15.75, 13),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.75, 7.75, 5),
                aabb(0.5, 0.5, 1),
                cull(0),
                rotate(
                        pivot(16.75, 15.75, 13),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8, 3.75, 8),
                aabb(0.5, 0.5, 4),
                cull(0),
                rotate(
                        pivot(16, 11.75, 16),
                        angle(-22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8, 5.25, 18.5),
                aabb(0.5, 4.0, 0.5),
                cull(0),
                rotate(
                        pivot(16, 13.25, 11),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(9, -3.2, 6.25),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(NORTH | WEST),
                rotate(
                        pivot(17, 4.9, 14.25),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(9, -3, 5.75),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(SOUTH | WEST),
                rotate(
                        pivot(17, 5, 13.75),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.5, -3, 5.75),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(SOUTH | EAST),
                rotate(
                        pivot(16.5, 5, 13.75),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7, -3.2, 6.25),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(NORTH | EAST),
                rotate(
                        pivot(15, 4.9, 14.25),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.5, -3.2, 6.25),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(NORTH | WEST),
                rotate(
                        pivot(15.5, 4.9, 14.25),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7, -3, 5.75),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(SOUTH | EAST),
                rotate(
                        pivot(15, 5, 13.75),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.5, -3, 5.75),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(SOUTH | WEST),
                rotate(
                        pivot(15.5, 5, 13.75),
                        angle(22.5, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.25, 4.9999999999999964, 8.350000000000001),
                aabb(1.0, 8.0, 1.5),
                cull(SOUTH | WEST),
                rotate(
                        pivot(16.25, 4.9999999999999964, 16.35),
                        angle(-45, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.2500000000000036, 5.999999999999979, 9.449999999999996),
                aabb(1.0, 8.000000000000023, 1.5),
                cull(NORTH | EAST),
                rotate(
                        pivot(-3.000000000000001, 5.9999999999999964, 17.350000000000005),
                        angle(-45, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(7.25, 4.9999999999999964, 8.350000000000001),
                aabb(1.0, 8.0, 1.5),
                cull(SOUTH | EAST),
                rotate(
                        pivot(15.25, 4.9999999999999964, 16.35),
                        angle(-45, 0, 0)
                )
        );
    }
}
