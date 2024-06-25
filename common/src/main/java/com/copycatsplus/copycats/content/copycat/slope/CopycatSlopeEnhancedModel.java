package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadSlope.map;

public class CopycatSlopeEnhancedModel implements SimpleCopycatPart {

    private static final double MARGIN = 2;
    private static final double MARGIN_ADJ = MARGIN / Math.tan(Math.toRadians(22.5));
    private static final double MID_LENGTH = 16 * Math.sqrt(2) - 2 * MARGIN_ADJ;
    private static final double ALIGNED_LENGTH = ((int) Math.floor(MID_LENGTH)) % 2 == 0 ? Math.floor(MID_LENGTH) : Math.floor(MID_LENGTH) + 1;

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getValue(CopycatSlopeBlock.FACING);
        Half half = state.getValue(CopycatSlopeBlock.HALF);
        int rot = (int) facing.toYRot();
        boolean flipY = half == Half.TOP;
        GlobalTransform transform = t -> t.flipY(flipY).rotateY(rot);
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, MARGIN_ADJ),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(0, MARGIN_ADJ, 0, MARGIN, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, MARGIN_ADJ),
                aabb(16, 16, 16 - MARGIN - MARGIN_ADJ).move(0, 0, MARGIN_ADJ),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(MARGIN_ADJ, 16 - MARGIN, MARGIN, 16 - MARGIN_ADJ, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - MARGIN),
                aabb(16, 16, MARGIN).move(0, 0, 16 - MARGIN),
                cull(UP | NORTH),
                updateUV(slope(Direction.UP, (a, b) -> map(16 - MARGIN, 16, 16 - MARGIN_ADJ, 16, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, MARGIN_ADJ),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(0, MARGIN_ADJ, 0, MARGIN, b))),
                translate(0, -16, 0),
                rotate(
                        pivot(0, 0, 0),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 16 - MARGIN, 16 - ALIGNED_LENGTH / 2),
                aabb(16, MARGIN, ALIGNED_LENGTH / 2).move(0, 16 - MARGIN, Math.floor((16 - ALIGNED_LENGTH / 2) / 2)),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, 16, 16),
                        scale(1, 1, MID_LENGTH / ALIGNED_LENGTH)
                ),
                translate(0, 0, -MARGIN_ADJ - MID_LENGTH / 2),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 16 - MARGIN, 16 - ALIGNED_LENGTH / 2),
                aabb(16, MARGIN, ALIGNED_LENGTH / 2).move(0, 16 - MARGIN, Math.floor((16 - ALIGNED_LENGTH / 2) / 2)),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, 16, 16),
                        scale(1, 1, MID_LENGTH / ALIGNED_LENGTH)
                ),
                translate(0, 0, -MARGIN_ADJ),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - MARGIN_ADJ),
                aabb(16, 16, MARGIN_ADJ).move(0, 0, 16 - MARGIN_ADJ),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(16 - MARGIN_ADJ, 16, MARGIN, 0, b))),
                rotate(
                        pivot(16, 16, 16),
                        angle(-45, 0, 0)
                )
        );
    }
}
