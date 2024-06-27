package com.copycatsplus.copycats.content.copycat.slope;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.SOUTH;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadSlope.map;

public class CopycatSlopeModel implements SimpleCopycatPart {

    private final boolean enhanced;

    public CopycatSlopeModel(boolean enhanced) {
        this.enhanced = enhanced;
    }

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Direction facing = state.getValue(CopycatSlopeBlock.FACING);
        Half half = state.getValue(CopycatSlopeBlock.HALF);
        int rot = (int) facing.toYRot();
        boolean flipY = half == Half.TOP;
        GlobalTransform transform = t -> t.flipY(flipY).rotateY(rot);
        assembleSlope(context, transform, 0, 16, enhanced);
    }

    public static void assembleSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double minHeight, double maxHeight, boolean enhanced) {
        if (minHeight == 0) {
            if (enhanced) {
                assembleTriangularSlope(context, transform, maxHeight, getMarginForHeight(maxHeight));
            } else {
                assembleTriangularSlope(context, transform, maxHeight);
            }
        } else {
            if (enhanced) {
                assembleTrapezoidSlope(context, transform, minHeight, maxHeight, Math.min(2, getMarginForHeight(maxHeight)));
            } else {
                assembleTrapezoidSlope(context, transform, minHeight, maxHeight);
            }
        }
    }

    private static double getMarginForHeight(double maxHeight) {
        if (maxHeight <= 2.5) return 0.5;
        if (maxHeight <= 4.5) return 1;
        if (maxHeight <= 8.5) return 2;
        return 3;
    }

    public static void assembleTriangularSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double maxHeight) {
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, 16),
                cull(NORTH),
                updateUV(slope(Direction.UP, (a, b) -> map(0, 16, 0, maxHeight, b)))
        );
    }

    public static void assembleTrapezoidSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double minHeight, double maxHeight) {
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, 16),
                cull(0),
                updateUV(slope(Direction.UP, (a, b) -> map(0, 16, minHeight, maxHeight, b)))
        );
    }

    public static void assembleTriangularSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double maxHeight, double margin) {
        final double angleBottom = Math.toDegrees(Math.atan2(maxHeight, 16));
        final double marginAdjBottom = margin / Math.tan(Math.toRadians(angleBottom) / 2);
        final double angleTop = Math.toDegrees(Math.atan2(16, maxHeight));
        final double marginAdjTop = margin / Math.tan(Math.toRadians(angleTop) / 2);

        final double halfLength = Math.sqrt(maxHeight * maxHeight + 16 * 16) / 2;

        final double midLengthBottom = halfLength - marginAdjBottom;
        final double marginAdjExcessBottom = marginAdjBottom - Math.floor(marginAdjBottom);
        final double midLengthTop = halfLength - marginAdjTop;
        final double marginAdjExcessTop = marginAdjTop - Math.floor(marginAdjTop);

        final double alignedLengthBottom = Math.abs(Math.floor(midLengthBottom + marginAdjExcessBottom) - marginAdjExcessBottom);
        final double alignedLengthTop = Math.floor(midLengthTop + marginAdjExcessTop) - marginAdjExcessTop;

        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, marginAdjBottom),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(0, marginAdjBottom, 0, margin, b)))
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, marginAdjBottom),
                aabb(16, 16, 16 - margin - marginAdjBottom).move(0, 0, marginAdjBottom),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(marginAdjBottom, 16 - margin, margin, maxHeight - marginAdjTop, b)))
        );
        if (maxHeight == 16) {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 16 - margin),
                    aabb(16, 16, margin).move(0, 0, 16 - margin),
                    cull(UP | NORTH),
                    updateUV(slope(Direction.UP, (a, b) -> map(16 - margin, 16, maxHeight - marginAdjTop, maxHeight, b)))
            );
        } else {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 16 - margin),
                    aabb(16, maxHeight / 2, margin).move(0, 0, 16 - margin),
                    cull(UP | NORTH)
            );
            assemblePiece(context,
                    transform,
                    vec3(0, 16 - maxHeight / 2, 16 - margin),
                    aabb(16, maxHeight / 2, margin).move(0, 16 - maxHeight / 2, 16 - margin),
                    cull(UP | DOWN | NORTH),
                    scale(
                            pivot(16, 16, 16),
                            scale(1, 32 / maxHeight, 1)
                    ),
                    updateUV(slope(Direction.UP, (a, b) -> map(16 - margin, 16, 16 - marginAdjTop / maxHeight * 32, 16, b))),
                    scale(
                            pivot(16, 16, 16),
                            scale(1, maxHeight / 32, 1)
                    ),
                    translate(0, -16 + maxHeight, 0)
            );
        }
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, marginAdjBottom),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(0, marginAdjBottom, 0, margin, b))),
                translate(0, -16, 0),
                rotate(
                        pivot(0, 0, 0),
                        angle(-angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, maxHeight - margin, 16 - alignedLengthBottom),
                aabb(16, margin, alignedLengthBottom).move(0, 16 - margin, marginAdjBottom),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, maxHeight, 16),
                        scale(1, 1, midLengthBottom / alignedLengthBottom)
                ),
                translate(0, 0, -halfLength),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(-angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, maxHeight - margin, 16 - alignedLengthTop),
                aabb(16, margin, alignedLengthTop).move(0, 16 - margin, 16 - marginAdjTop - alignedLengthTop),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, maxHeight, 16),
                        scale(1, 1, midLengthTop / alignedLengthTop)
                ),
                translate(0, 0, -marginAdjTop),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(-angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - marginAdjTop),
                aabb(16, 16, marginAdjTop).move(0, 0, 16 - marginAdjTop),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(16 - marginAdjTop, 16, margin, 0, b))),
                translate(0, -16 + maxHeight, 0),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(-angleBottom, 0, 0)
                )
        );
    }

    public static void assembleTrapezoidSlope(CopycatRenderContext<?, ?> context, GlobalTransform transform, double minHeight, double maxHeight, double margin) {
        final double angleBottom = Math.toDegrees(Math.atan2(maxHeight - minHeight, 16)) + 90;
        final double marginAdjBottom = margin / Math.tan(Math.toRadians(angleBottom) / 2);
        final double angleTop = Math.toDegrees(Math.atan2(16, maxHeight - minHeight));
        final double marginAdjTop = margin / Math.tan(Math.toRadians(angleTop) / 2);

        final double halfLength = Math.sqrt((maxHeight - minHeight) * (maxHeight - minHeight) + 16 * 16) / 2;

        final double midLengthBottom = halfLength - marginAdjBottom;
        final double marginAdjExcessBottom = marginAdjBottom - Math.floor(marginAdjBottom);
        final double midLengthTop = halfLength - marginAdjTop;
        final double marginAdjExcessTop = marginAdjTop - Math.floor(marginAdjTop);

        final double alignedLengthBottom = Math.abs(Math.floor(midLengthBottom + marginAdjExcessBottom) - marginAdjExcessBottom);
        final double alignedLengthTop = Math.floor(midLengthTop + marginAdjExcessTop) - marginAdjExcessTop;

        if (minHeight == 16 || minHeight == 0) {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, 16, margin),
                    cull(UP | SOUTH),
                    updateUV(slope(Direction.UP, (a, b) -> map(0, margin, minHeight, minHeight - marginAdjBottom, b)))
            );
        } else {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, minHeight / 2, margin).move(0, 0, 0),
                    cull(UP | SOUTH)
            );
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 0),
                    aabb(16, minHeight / 2, margin).move(0, 16 - minHeight / 2, 0),
                    cull(UP | DOWN | SOUTH),
                    scale(
                            pivot(0, 0, 0),
                            scale(1, 32 / minHeight, 1)
                    ),
                    updateUV(slope(Direction.UP, (a, b) -> map(0, margin, 16, 16 - marginAdjBottom / minHeight * 32, b))),
                    scale(
                            pivot(0, 0, 0),
                            scale(1, minHeight / 32, 1)
                    ),
                    translate(0, minHeight / 2, 0)
            );
        }
        assemblePiece(context,
                transform,
                vec3(0, 0, margin),
                aabb(16, 16, 16 - margin * 2).move(0, 0, margin),
                cull(UP | NORTH | SOUTH),
                updateUV(slope(Direction.UP, (a, b) -> map(margin, 16 - margin, minHeight - marginAdjBottom, maxHeight - marginAdjTop, b)))
        );
        if (maxHeight == 16 || maxHeight == 0) {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 16 - margin),
                    aabb(16, 16, margin).move(0, 0, 16 - margin),
                    cull(UP | NORTH),
                    updateUV(slope(Direction.UP, (a, b) -> map(16 - margin, 16, maxHeight - marginAdjTop, maxHeight, b)))
            );
        } else {
            assemblePiece(context,
                    transform,
                    vec3(0, 0, 16 - margin),
                    aabb(16, maxHeight / 2, margin).move(0, 0, 16 - margin),
                    cull(UP | NORTH)
            );
            assemblePiece(context,
                    transform,
                    vec3(0, 16 - maxHeight / 2, 16 - margin),
                    aabb(16, maxHeight / 2, margin).move(0, 16 - maxHeight / 2, 16 - margin),
                    cull(UP | DOWN | NORTH),
                    scale(
                            pivot(16, 16, 16),
                            scale(1, 32 / maxHeight, 1)
                    ),
                    updateUV(slope(Direction.UP, (a, b) -> map(16 - margin, 16, 16 - marginAdjTop / maxHeight * 32, 16, b))),
                    scale(
                            pivot(16, 16, 16),
                            scale(1, maxHeight / 32, 1)
                    ),
                    translate(0, -16 + maxHeight, 0)
            );
        }
        assemblePiece(context,
                transform,
                vec3(0, 0, 0),
                aabb(16, 16, marginAdjBottom),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(0, marginAdjBottom, 0, margin, b))),
                translate(0, -16 + minHeight, 0),
                rotate(
                        pivot(0, minHeight, 0),
                        angle(90 - angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, maxHeight - margin, 16 - alignedLengthBottom),
                aabb(16, margin, alignedLengthBottom).move(0, 16 - margin, marginAdjBottom),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, maxHeight, 16),
                        scale(1, 1, midLengthBottom / alignedLengthBottom)
                ),
                translate(0, 0, -halfLength),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(90 - angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, maxHeight - margin, 16 - alignedLengthTop),
                aabb(16, margin, alignedLengthTop).move(0, 16 - margin, 16 - marginAdjTop - alignedLengthTop),
                cull(DOWN | NORTH | SOUTH),
                scale(
                        pivot(16, maxHeight, 16),
                        scale(1, 1, midLengthTop / alignedLengthTop)
                ),
                translate(0, 0, -marginAdjTop),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(90 - angleBottom, 0, 0)
                )
        );
        assemblePiece(context,
                transform,
                vec3(0, 0, 16 - marginAdjTop),
                aabb(16, 16, marginAdjTop).move(0, 0, 16 - marginAdjTop),
                cull(DOWN | NORTH | SOUTH),
                updateUV(slope(Direction.DOWN, (a, b) -> map(16 - marginAdjTop, 16, margin, 0, b))),
                translate(0, -16 + maxHeight, 0),
                rotate(
                        pivot(16, maxHeight, 16),
                        angle(90 - angleBottom, 0, 0)
                )
        );
    }
}
