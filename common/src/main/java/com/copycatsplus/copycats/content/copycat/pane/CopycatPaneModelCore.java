package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatPaneModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(IronBarsBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof IronBarsBlock) {
            context.assembleAll();
            return;
        }

        Set<Direction> present = Arrays.stream(Iterate.horizontalDirections).filter(dir -> state.getValue(CopycatPaneBlock.propertyForDirection(dir))).collect(Collectors.toSet());

        if (present.size() == 2 && present.contains(Direction.NORTH) && present.contains(Direction.SOUTH)) {
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(7, 0, 0),
                    aabb(1, 16, 16).move(0, 0, 0),
                    cull(EAST)
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(8, 0, 0),
                    aabb(1, 16, 16).move(15, 0, 0),
                    cull(WEST)
            );
            return;
        } else if (present.size() == 2 && present.contains(Direction.EAST) && present.contains(Direction.WEST)) {
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(0, 0, 7),
                    aabb(16, 16, 1).move(0, 0, 0),
                    cull(SOUTH)
            );
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(0, 0, 8),
                    aabb(16, 16, 1).move(0, 0, 15),
                    cull(NORTH)
            );
            return;
        } else if (present.size() == 1) {
            Direction dir = present.iterator().next();
            AssemblyTransform directionTransform = t -> t.rotateY((int) dir.toYRot());
            context.assemblePiece(directionTransform,
                    vec3(7, 0, 7),
                    aabb(1, 16, 9).move(0, 0, 7),
                    cull(EAST | NORTH)
            );
            context.assemblePiece(directionTransform,
                    vec3(8, 0, 7),
                    aabb(1, 16, 9).move(15, 0, 7),
                    cull(WEST | NORTH)
            );
            context.assemblePiece(directionTransform,
                    vec3(7, 0, 7),
                    aabb(1, 16, 9).move(7, 0, 7),
                    cull(UP | DOWN | EAST | WEST | SOUTH)
            );
            context.assemblePiece(directionTransform,
                    vec3(8, 0, 7),
                    aabb(1, 16, 9).move(8, 0, 7),
                    cull(UP | DOWN | EAST | WEST | SOUTH)
            );
            return;
        }

        for (Direction direction : Iterate.horizontalDirections) {
            AssemblyTransform directionTransform = t -> t.rotateY((int) direction.toYRot());
            context.assemblePiece(directionTransform,
                    vec3(7, 0, 8),
                    aabb(1, 16, 1).move(0, 0, 8),
                    cull((present.contains(direction.getClockWise()) ? WEST : 0) |
                            SOUTH | NORTH | EAST)
            );
            if (!present.contains(direction)) {
                context.assemblePiece(directionTransform,
                        vec3(7, 0, 8),
                        aabb(1, 16, 1).move(7, 0, 15),
                        cull(NORTH | EAST | WEST | UP | DOWN)
                );
            }
        }

        for (Direction direction : present) {
            AssemblyTransform directionTransform = t -> t.rotateY((int) direction.toYRot());
            context.assemblePiece(directionTransform,
                    vec3(7, 0, 9),
                    aabb(1, 16, 7).move(0, 0, 9),
                    cull(EAST | NORTH)
            );
            context.assemblePiece(directionTransform,
                    vec3(8, 0, 9),
                    aabb(1, 16, 7).move(15, 0, 9),
                    cull(WEST | NORTH)
            );
        }
    }
}
