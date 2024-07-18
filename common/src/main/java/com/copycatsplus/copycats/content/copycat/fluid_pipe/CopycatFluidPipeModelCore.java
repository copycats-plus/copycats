package com.copycatsplus.copycats.content.copycat.fluid_pipe;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatFluidPipeModelCore extends CopycatModelCore.WithData<CopycatFluidPipeModelCore.PipeModelData> {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        super.registerModels(entries);
        entries.add(new ModelEntry("bracket", (state, material) -> getData().getBracket(), null, EntryType.STATIC));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        List<Direction> directions = new ArrayList<>(6);
        for (Direction direction : Iterate.directions) {
            if (state.getValue(PipeBlock.PROPERTY_BY_DIRECTION.get(direction)))
                directions.add(direction);
        }
        if (directions.size() == 2) {
            if (directions.get(0).getAxis() == directions.get(1).getAxis()) {
                int yRot = directions.get(0).getAxis() == Axis.X ? 90 : 0;
                int xRot = directions.get(0).getAxis() == Axis.Y ? 90 : 0;
                renderStraightCore(context, t -> t.rotateY(yRot).rotateX(xRot));
            } else {
                Direction base = null;
                if (directions.get(0).getAxis() == Axis.X) {
                    base = directions.remove(0);
                } else if (directions.get(1).getAxis() == Axis.X) {
                    base = directions.remove(1);
                }
                if (base != null) {
                    boolean flipX = base.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                    int xRot = getXRot(directions.get(0));
                    renderBend(context, t -> t.flipX(flipX).rotateX(xRot));
                } else {
                    if (directions.get(0).getAxis() == Axis.Z) {
                        base = directions.remove(0);
                    } else if (directions.get(1).getAxis() == Axis.Z) {
                        base = directions.remove(1);
                    }
                    if (base != null) {
                        boolean flipZ = base.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                        int zRot = getZRot(directions.get(0));
                        renderBend(context, t -> t.flipZ(flipZ).rotateZ(zRot));
                    }
                }
            }
        } else if (directions.size() == 3) {
            boolean flipX = false, flipY = false, flipZ = false;
            for (Direction direction : directions) {
                if (direction.getAxis() == Axis.X) {
                    flipX = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
                } else if (direction.getAxis() == Axis.Y) {
                    flipY = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
                } else {
                    flipZ = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
                }
            }
            boolean finalFlipX = flipX;
            boolean finalFlipY = flipY;
            boolean finalFlipZ = flipZ;
            renderCorner(context, t -> t.flipX(finalFlipX).flipY(finalFlipY).flipZ(finalFlipZ));
        }
        assembleAccessories(context);
    }

    protected void assembleAccessories(CopycatRenderContext context) {
        for (Direction direction : Iterate.directions) {
            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials partial : getData().getAttachment(direction).partials) {
                renderComponent(context, direction, partial);
            }
        }
        if (getData().isEncased()) {
            renderEncasing(context);
        }
    }

    protected static int getXRot(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case DOWN -> 90;
            case SOUTH -> 180;
            case UP -> 270;
            default -> 0;
        };
    }

    protected static int getZRot(Direction direction) {
        return switch (direction) {
            case WEST -> 0;
            case UP -> 90;
            case EAST -> 180;
            case DOWN -> 270;
            default -> 0;
        };
    }

    protected void renderStraightCore(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(
                transform,
                vec3(4, 4, 4),
                aabb(4, 4, 8).move(0, 0, 4),
                cull(EAST | UP | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(8, 4, 4),
                aabb(4, 4, 8).move(12, 0, 4),
                cull(WEST | UP | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(4, 8, 4),
                aabb(4, 4, 8).move(0, 12, 4),
                cull(EAST | DOWN | NORTH | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(8, 8, 4),
                aabb(4, 4, 8).move(12, 12, 4),
                cull(WEST | DOWN | NORTH | SOUTH)
        );
    }

    protected void renderBend(CopycatRenderContext context, AssemblyTransform transform) {
        if (enhanced) {
            context.assemblePiece(
                    transform,
                    vec3(8, 4, 4),
                    aabb(4, 4, 8).move(12, 0, 8),
                    cull(WEST | UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 8, 4),
                    aabb(4, 4, 8).move(12, 12, 8),
                    cull(WEST | DOWN | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 8),
                    aabb(4, 4, 4).move(8, 0, 12),
                    cull(EAST | WEST | UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 8, 8),
                    aabb(4, 4, 4).move(8, 12, 12),
                    cull(EAST | WEST | DOWN | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(7, 4, 4),
                    aabb(1, 8, 4).move(3, 0, 8),
                    cull(EAST | WEST | NORTH | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(5, 4, 4),
                    aabb(2, 8, 2).move(1, 0, 8),
                    cull(EAST | WEST | NORTH | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 6),
                    aabb(3, 8, 2).move(8, 0, 2),
                    cull(EAST | WEST | NORTH | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 4),
                    aabb(1, 8, 2).move(8, 0, 0),
                    cull(EAST | WEST | NORTH | SOUTH)
            );
        } else {
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 4),
                    aabb(8, 4, 8).move(8, 0, 8),
                    cull(WEST | UP | NORTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 8, 4),
                    aabb(8, 4, 8).move(8, 12, 8),
                    cull(WEST | DOWN | NORTH)
            );
        }
    }

    protected void renderCorner(CopycatRenderContext context, AssemblyTransform transform) {
        if (enhanced) {
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 4),
                    aabb(4, 8, 4).move(0, 0, 0),
                    cull(EAST | UP | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 8),
                    aabb(4, 4, 4).move(0, 0, 4),
                    cull(EAST | UP | NORTH | SOUTH)
            );
            context.assemblePiece(
                    transform,
                    vec3(8, 4, 4),
                    aabb(4, 4, 4).move(4, 0, 0),
                    cull(EAST | WEST | UP | SOUTH)
            );
            renderCornerPart(context, transform);
            renderCornerPart(context, t -> transform.apply(t.rotateY(-90).flipZ(true)));
            renderCornerPart(context, t -> transform.apply(t.rotateX(90).flipZ(true)));
        } else {
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 4),
                    aabb(8, 8, 8).move(0, 0, 0),
                    cull(EAST | UP | SOUTH)
            );
        }
    }

    protected void renderCornerPart(CopycatRenderContext context, AssemblyTransform transform) {
        context.assemblePiece(
                transform,
                vec3(8, 8, 4),
                aabb(4, 1, 4).move(4, 12, 0),
                cull(EAST | WEST | UP | DOWN | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(10, 9, 4),
                aabb(2, 2, 4).move(6, 13, 0),
                cull(EAST | WEST | UP | DOWN | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(8, 9, 4),
                aabb(2, 3, 4).move(12, 5, 0),
                cull(EAST | WEST | UP | DOWN | SOUTH)
        );
        context.assemblePiece(
                transform,
                vec3(10, 11, 4),
                aabb(2, 1, 4).move(14, 7, 0),
                cull(EAST | WEST | UP | DOWN | SOUTH)
        );
    }

    protected void renderEncasing(CopycatRenderContext context) {
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(3, 3, 3),
                aabb(5, 5, 5).move(0, 0, 0),
                cull(EAST | UP | SOUTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(8, 3, 3),
                aabb(5, 5, 5).move(11, 0, 0),
                cull(WEST | UP | SOUTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(3, 8, 3),
                aabb(5, 5, 5).move(0, 11, 0),
                cull(EAST | DOWN | SOUTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(8, 8, 3),
                aabb(5, 5, 5).move(11, 11, 0),
                cull(WEST | DOWN | SOUTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(3, 3, 8),
                aabb(5, 5, 5).move(0, 0, 11),
                cull(EAST | UP | NORTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(8, 3, 8),
                aabb(5, 5, 5).move(11, 0, 11),
                cull(WEST | UP | NORTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(3, 8, 8),
                aabb(5, 5, 5).move(0, 11, 11),
                cull(EAST | DOWN | NORTH)
        );
        context.assemblePiece(
                AssemblyTransform.IDENTITY,
                vec3(8, 8, 8),
                aabb(5, 5, 5).move(11, 11, 11),
                cull(WEST | DOWN | NORTH)
        );
    }

    protected void renderComponent(CopycatRenderContext context, Direction direction, FluidTransportBehaviour.AttachmentTypes.ComponentPartials component) {
        AssemblyTransform transform = direction.getAxis().isVertical()
                ? t -> t.rotateX(direction == Direction.DOWN ? 90 : -90)
                : t -> t.rotateY((int) direction.toYRot() + 180);
        switch (component) {
            case RIM -> {
                context.assemblePiece(
                        transform,
                        vec3(3, 3, 0),
                        aabb(5, 5, 2).move(0, 0, 14),
                        cull(EAST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 3, 0),
                        aabb(5, 5, 2).move(11, 0, 14),
                        cull(WEST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(3, 8, 0),
                        aabb(5, 5, 2).move(0, 11, 14),
                        cull(EAST | DOWN)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, 0),
                        aabb(5, 5, 2).move(11, 11, 14),
                        cull(WEST | DOWN)
                );
            }
            case CONNECTION -> {
                context.assemblePiece(
                        transform,
                        vec3(4, 4, 0),
                        aabb(4, 4, 4).move(0, 0, 4),
                        cull(EAST | UP | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 4, 0),
                        aabb(4, 4, 4).move(12, 0, 4),
                        cull(WEST | UP | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(4, 8, 0),
                        aabb(4, 4, 4).move(0, 12, 4),
                        cull(EAST | DOWN | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, 0),
                        aabb(4, 4, 4).move(12, 12, 4),
                        cull(WEST | DOWN | SOUTH | NORTH)
                );
            }
            case RIM_CONNECTOR -> {
                context.assemblePiece(
                        transform,
                        vec3(4, 4, 2),
                        aabb(4, 4, 2).move(0, 0, 4),
                        cull(EAST | UP | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 4, 2),
                        aabb(4, 4, 2).move(12, 0, 4),
                        cull(WEST | UP | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(4, 8, 2),
                        aabb(4, 4, 2).move(0, 12, 4),
                        cull(EAST | DOWN | SOUTH | NORTH)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, 2),
                        aabb(4, 4, 2).move(12, 12, 4),
                        cull(WEST | DOWN | SOUTH | NORTH)
                );
            }
            case DRAIN -> {
                context.assemblePiece(
                        transform,
                        vec3(3, 3, -1),
                        aabb(5, 5, 3).move(0, 0, 13),
                        cull(EAST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 3, -1),
                        aabb(5, 5, 3).move(11, 0, 13),
                        cull(WEST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(3, 8, -1),
                        aabb(5, 5, 3).move(0, 11, 13),
                        cull(EAST | DOWN)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, -1),
                        aabb(5, 5, 3).move(11, 11, 13),
                        cull(WEST | DOWN)
                );

                context.assemblePiece(
                        transform,
                        vec3(5, 5, -4),
                        aabb(3, 3, 3).move(0, 0, 0),
                        cull(EAST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 5, -4),
                        aabb(3, 3, 3).move(13, 0, 0),
                        cull(WEST | UP)
                );
                context.assemblePiece(
                        transform,
                        vec3(5, 8, -4),
                        aabb(3, 3, 3).move(0, 13, 0),
                        cull(EAST | DOWN)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, -4),
                        aabb(3, 3, 3).move(13, 13, 0),
                        cull(WEST | DOWN)
                );
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class PipeModelData {
        private final FluidTransportBehaviour.AttachmentTypes[] attachments;
        private boolean encased;
        private BakedModel bracket;

        public PipeModelData() {
            attachments = new FluidTransportBehaviour.AttachmentTypes[6];
            Arrays.fill(attachments, FluidTransportBehaviour.AttachmentTypes.NONE);
        }

        public void putBracket(BlockState state) {
            if (state != null) {
                this.bracket = Minecraft.getInstance()
                        .getBlockRenderer()
                        .getBlockModel(state);
            }
        }

        public BakedModel getBracket() {
            return bracket;
        }

        public void putAttachment(Direction face, FluidTransportBehaviour.AttachmentTypes rim) {
            attachments[face.get3DDataValue()] = rim;
        }

        public FluidTransportBehaviour.AttachmentTypes getAttachment(Direction face) {
            return attachments[face.get3DDataValue()];
        }

        public void setEncased(boolean encased) {
            this.encased = encased;
        }

        public boolean isEncased() {
            return encased;
        }
    }
}
