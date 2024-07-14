package com.copycatsplus.copycats.content.copycat.copy_cat;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatCopyCatModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        int rot = (int) state.getValue(CopycatCopyCatBlock.FACING).toYRot();
        AssemblyTransform transform = t -> t.rotateY(rot);
//Left Front Leg

        context.assemblePiece(transform,
                vec3(8.75, -0.9500000000000002, 6.500000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(16.5, 4.9, 14.25),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.SOUTH, 15f, 11.5f),
                uvTranslate(Direction.WEST, 16f, 11.5f)
        );


//Left Back Leg

        context.assemblePiece(transform,
                vec3(9.0, 0.5, 8.5),
                aabb(1.0, 1, 3),
                cull(0),
                rotate(
                        pivot(16.5, 8, 2),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 14.000000000000002f, 7f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 14.000000000000002f, 2f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 10.999999999999996f, 10.000000000000005f),
                uvRotate(Direction.NORTH, 8f, 8f, 180),
                uvTranslate(Direction.SOUTH, 10.999999999999996f, 3.0000000000000013f),
                uvTranslate(Direction.EAST, 14f, 4f),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvTranslate(Direction.WEST, 14f, 10f),
                uvRotate(Direction.WEST, 8f, 8f, 90)
        );


//Right Back Leg

        context.assemblePiece(transform,
                vec3(7.5, 0.5, 8.5),
                aabb(1, 1, 3),
                cull(0),
                rotate(
                        pivot(15, 6, 2),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 13.999999999999998f, 2f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 14f, 10f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 11f, 10f),
                uvRotate(Direction.NORTH, 8f, 8f, 180),
                uvTranslate(Direction.SOUTH, 11f, 2.9999999999999996f),
                uvTranslate(Direction.EAST, 14f, 7.5f),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvTranslate(Direction.WEST, 14f, 4.500000000000001f),
                uvRotate(Direction.WEST, 8f, 8f, 90)
        );


//Back

        context.assemblePiece(transform,
                vec3(8.750000000000004, 9.97499999999999, 10.174999999999995),
                aabb(1.0, 8.000000000000025, 1.5),
                cull(0),
                rotate(
                        pivot(-2.000000000000001, 5.974999999999997, 17.325000000000006),
                        angle(-45, 0, 0)
                ),
                uvTranslate(Direction.UP, 6f, 0f),
                uvRotate(Direction.UP, 8f, 8f, 270),
                uvTranslate(Direction.DOWN, 5.5f, 15f),
                uvRotate(Direction.DOWN, 8f, 8f, 90),
                uvRotate(Direction.NORTH, 8f, 8f, 270),
                uvTranslate(Direction.SOUTH, 14f, 0f),
                uvTranslate(Direction.EAST, 4f, 13.5f),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvRotate(Direction.WEST, 8f, 8f, 180)
        );


//Tail

        context.assemblePiece(transform,
                vec3(8.25, 4.0, 10.0),
                aabb(0.5, 0.5, 4),
                cull(0),
                rotate(
                        pivot(16, 11.75, 16),
                        angle(-22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 8.400000000000002f, 0f),
                uvRotate(Direction.UP, 8f, 8f, 90),
                uvTranslate(Direction.DOWN, 4.5f, 0f),
                uvRotate(Direction.DOWN, 8f, 8f, 90),
                uvTranslate(Direction.NORTH, 15.5f, 0f),
                uvTranslate(Direction.SOUTH, 15f, 0.5f),
                uvTranslate(Direction.EAST, 8.4f, 0.5f),
                uvTranslate(Direction.WEST, 4.499999999999999f, 0.5f)
        );

        context.assemblePiece(transform,
                vec3(8.25, 7.25, 18.75),
                aabb(0.5, 4.0, 0.5),
                cull(0),
                rotate(
                        pivot(16, 13.25, 11),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 1f, 1.5f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 0.75f, 0f),
                uvRotate(Direction.NORTH, 8f, 8f, 90),
                uvTranslate(Direction.SOUTH, 0.75f, 0f),
                uvRotate(Direction.SOUTH, 8f, 8f, 90),
                uvTranslate(Direction.EAST, 0f, 0.5250000000000004f),
                uvTranslate(Direction.WEST, 0.75f, 0.5f),
                uvRotate(Direction.WEST, 8f, 8f, 90)
        );


//Left Front Leg

        context.assemblePiece(transform,
                vec3(9.25, -0.9500000000000002, 6.500000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(17, 4.9, 14.25),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.SOUTH, 15f, 11.5f),
                uvTranslate(Direction.EAST, 16f, 11.5f)
        );

        context.assemblePiece(transform,
                vec3(9.25, -0.75, 6.000000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(17, 5, 13.75),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.NORTH, 15f, 11.5f),
                uvTranslate(Direction.EAST, 16f, 11.5f)
        );

        context.assemblePiece(transform,
                vec3(8.75, -0.75, 6.000000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(16.5, 5, 13.75),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.NORTH, 15f, 11.5f),
                uvTranslate(Direction.WEST, 16f, 11.5f)
        );


//Right Front Leg

        context.assemblePiece(transform,
                vec3(7.250000000000001, -0.9500000000000002, 6.500000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(15, 4.9, 14.25),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.SOUTH, 15f, 11.5f),
                uvTranslate(Direction.WEST, 16f, 11.5f)
        );

        context.assemblePiece(transform,
                vec3(7.750000000000001, -0.9500000000000002, 6.500000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(15.5, 4.9, 14.25),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.SOUTH, 15f, 11.5f),
                uvTranslate(Direction.EAST, 16f, 11.5f)
        );

        context.assemblePiece(transform,
                vec3(7.250000000000001, -0.75, 6.000000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(15, 5, 13.75),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.NORTH, 15f, 11.5f),
                uvTranslate(Direction.WEST, 16f, 11.5f)
        );

        context.assemblePiece(transform,
                vec3(7.750000000000001, -0.75, 6.000000000000001),
                aabb(0.5000000000000018, 4.5, 0.5000000000000018),
                cull(0),
                rotate(
                        pivot(15.5, 5, 13.75),
                        angle(22.5, 0, 0)
                ),
                uvTranslate(Direction.UP, 15f, 0f),
                uvTranslate(Direction.DOWN, 15f, 15f),
                uvTranslate(Direction.NORTH, 15f, 11.5f),
                uvTranslate(Direction.EAST, 16f, 11.5f)
        );


//Back

        context.assemblePiece(transform,
                vec3(8.75, 8.999999999999996, 9.100000000000001),
                aabb(1.0, 8.0, 1.5),
                cull(0),
                rotate(
                        pivot(16.25, 4.9999999999999964, 16.35),
                        angle(-45, 0, 0)
                ),
                uvTranslate(Direction.UP, 1f, 0f),
                uvRotate(Direction.UP, 8f, 8f, 270),
                uvTranslate(Direction.DOWN, 11.5f, 15f),
                uvRotate(Direction.DOWN, 8f, 8f, 90),
                uvTranslate(Direction.NORTH, 4f, 14f),
                uvRotate(Direction.NORTH, 8f, 8f, 270),
                uvTranslate(Direction.EAST, 4f, 1f),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvRotate(Direction.WEST, 8f, 8f, 180)
        );

        context.assemblePiece(transform,
                vec3(7.7500000000000036, 9.97499999999999, 10.174999999999995),
                aabb(1.0, 8.000000000000023, 1.5),
                cull(0),
                rotate(
                        pivot(-3.000000000000001, 5.974999999999996, 17.325000000000006),
                        angle(-45, 0, 0)
                ),
                uvTranslate(Direction.UP, 8f, 0f),
                uvRotate(Direction.UP, 8f, 8f, 270),
                uvTranslate(Direction.DOWN, 2.5f, 15f),
                uvRotate(Direction.DOWN, 8f, 8f, 90),
                uvRotate(Direction.NORTH, 8f, 8f, 270),
                uvTranslate(Direction.SOUTH, 14f, 0f),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvTranslate(Direction.WEST, 1f, 4f),
                uvRotate(Direction.WEST, 8f, 8f, 180)
        );

        context.assemblePiece(transform,
                vec3(7.75, 8.999999999999996, 9.100000000000001),
                aabb(1.0, 8.0, 1.5),
                cull(0),
                rotate(
                        pivot(15.25, 4.9999999999999964, 16.35),
                        angle(-45, 0, 0)
                ),
                uvTranslate(Direction.UP, 13f, 0f),
                uvRotate(Direction.UP, 8f, 8f, 270),
                uvTranslate(Direction.DOWN, 8.5f, 15f),
                uvRotate(Direction.DOWN, 8f, 8f, 90),
                uvTranslate(Direction.NORTH, 4f, 14f),
                uvRotate(Direction.NORTH, 8f, 8f, 270),
                uvRotate(Direction.EAST, 8f, 8f, 90),
                uvTranslate(Direction.WEST, 13.5f, 4f),
                uvRotate(Direction.WEST, 8f, 8f, 180)
        );


//Head

        context.assemblePiece(transform,
                vec3(7.25, 7.25, 4.75),
                aabb(0.5, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(15, 14.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 0f, 3f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 0.3000000000000007f, 0.5f),
                uvTranslate(Direction.SOUTH, 9f, 0f),
                uvTranslate(Direction.WEST, 6f, 0f)
        );

        context.assemblePiece(transform,
                vec3(9.250000000000002, 7.25, 4.75),
                aabb(0.5, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(16.999999999999993, 14.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 0f, 2f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 0.30000000000000004f, 0.5f),
                uvTranslate(Direction.SOUTH, 9f, 0f),
                uvTranslate(Direction.EAST, 8.5f, 0f)
        );

        context.assemblePiece(transform,
                vec3(8.25, 6.25, 3.25),
                aabb(1.5, 1.0, 0.5),
                cull(0),
                rotate(
                        pivot(15.5, 13.75, 11),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 6f, 15.5f),
                uvTranslate(Direction.DOWN, 3f, 15.5f),
                uvTranslate(Direction.NORTH, 5f, 1f),
                uvTranslate(Direction.EAST, 1f, 14f),
                uvTranslate(Direction.WEST, 1f, 14f)
        );

        context.assemblePiece(transform,
                vec3(7.5, 8.0, 5.5),
                aabb(0.5, 0.5, 1),
                cull(0),
                rotate(
                        pivot(15.25, 15.75, 13),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 1f, 1f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 1f, 1.5f),
                uvTranslate(Direction.SOUTH, 1f, 1f),
                uvTranslate(Direction.EAST, 1f, 1f),
                uvRotate(Direction.EAST, 8f, 8f, 270),
                uvTranslate(Direction.WEST, 1f, 1f),
                uvRotate(Direction.WEST, 8f, 8f, 90)
        );

        context.assemblePiece(transform,
                vec3(9.0, 8.0, 5.5),
                aabb(0.5, 0.5, 1),
                cull(0),
                rotate(
                        pivot(16.75, 15.75, 13),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 1f, 1f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 1f, 1.5f),
                uvTranslate(Direction.SOUTH, 1f, 1f),
                uvTranslate(Direction.EAST, 1f, 1f),
                uvRotate(Direction.EAST, 8f, 8f, 270),
                uvTranslate(Direction.WEST, 1f, 1f),
                uvRotate(Direction.WEST, 8f, 8f, 90)
        );

        context.assemblePiece(transform,
                vec3(8.625, 7.25, 4.75),
                aabb(0.75, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(16.25, 14.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 0f, 2f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 1.550000000000006f, 0.5f),
                uvTranslate(Direction.SOUTH, 9f, 0f)
        );

        context.assemblePiece(transform,
                vec3(7.875, 7.25, 4.75),
                aabb(0.75, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(15.5, 14.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvTranslate(Direction.UP, 0f, 2f),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 13.700000000000003f, 0.4999999999999982f),
                uvTranslate(Direction.SOUTH, 9f, 0f)
        );

        context.assemblePiece(transform,
                vec3(9.250000000000002, 6.25, 4.75),
                aabb(0.5, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(16.999999999999993, 13.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 0f, 2.5f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 0.3000000000000007f, 14.499999999999998f),
                uvTranslate(Direction.SOUTH, 9f, 0f),
                uvTranslate(Direction.EAST, 8.5f, 0f)
        );

        context.assemblePiece(transform,
                vec3(8.625, 6.25, 4.75),
                aabb(0.75, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(16.25, 13.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 0f, 2.5f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 9.5f, 14f),
                uvTranslate(Direction.SOUTH, 9f, 0f)
        );

        context.assemblePiece(transform,
                vec3(7.875, 6.25, 4.75),
                aabb(0.75, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(15.5, 13.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 0f, 2.5f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 9.5f, 14f),
                uvTranslate(Direction.SOUTH, 9f, 0f)
        );

        context.assemblePiece(transform,
                vec3(7.25, 6.25, 4.75),
                aabb(0.5, 1.0, 2.5),
                cull(0),
                rotate(
                        pivot(15, 13.75, 11.5),
                        angle(0, 0, 0)
                ),
                uvRotate(Direction.UP, 8f, 8f, 180),
                uvTranslate(Direction.DOWN, 0f, 2.5f),
                uvRotate(Direction.DOWN, 8f, 8f, 180),
                uvTranslate(Direction.NORTH, 0.09999999999999964f, 14.5f),
                uvTranslate(Direction.SOUTH, 9f, 0f),
                uvTranslate(Direction.WEST, 6f, 0f)
        );
    }
}
