package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatCogWheelModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(CogWheelBlock.class), EntryType.KINETIC_COPYCAT));
        entries.add(new ModelEntry("cogwheel", ModelGetter.MATERIAL, this, updatePropertiesIfMatch(CogWheelBlock.class), EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Axis axis = state.getValue(CopycatShaftBlock.AXIS);

        if (material.getBlock() instanceof CogWheelBlock) {
            context.assemblePiece(
                    t -> t.rotateX(axis == Axis.Z ? 90 : 0).rotateZ(axis == Axis.X ? 90 : 0),
                    vec3(-8, -8, -8),
                    aabb(32, 32, 32).move(-8, -8, -8),
                    cull(0),
                    noCull(),
                    (quad, sprite) -> {
                        for (MutableVertex vertex : quad.vertices) {
                            if (vertex.xyz.y < 0.01 || vertex.xyz.y > 0.99)
                                return false;
                        }
                        return true;
                    }
            );
            return;
        }

        for (int i = 0; i < 4; i++) {
            int rotation = i * 90;
            AssemblyTransform transform = t -> t.rotateZ(rotation).rotateY(axis == Axis.X ? 90 : 0).rotateX(axis == Axis.Y ? 90 : 0);
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 6),
                    aabb(4, 4, 2),
                    cull(EAST | SOUTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(4, 4, 8),
                    aabb(4, 4, 2).move(0, 0, 14),
                    cull(EAST | NORTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(2, 2, 6.55),
                    aabb(6, 6, 1.45),
                    cull(EAST | SOUTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(2, 2, 8),
                    aabb(6, 6, 1.45).move(0, 0, 14.55),
                    cull(EAST | NORTH | UP),
                    noCull()
            );
            for (int j = 0; j < 2; j++) {
                int gearRotation = j * 45;
                context.assemblePiece(
                        transform,
                        vec3(6.5, 0, 6.5),
                        aabb(1.5, 16, 1.5),
                        cull(EAST | SOUTH),
                        scale(
                                pivot(8, 8, 8),
                                scale(1, 18 / 16.0, 1 + j * 0.02)
                        ),
                        rotate(
                                pivot(8, 8, 8),
                                angle(0, 0, gearRotation)
                        ),
                        noCull()
                );
                context.assemblePiece(
                        transform,
                        vec3(6.5, 0, 8),
                        aabb(1.5, 16, 1.5).move(0, 0, 14.5),
                        cull(EAST | NORTH),
                        scale(
                                pivot(8, 8, 8),
                                scale(1, 18 / 16.0, 1 + j * 0.02)
                        ),
                        rotate(
                                pivot(8, 8, 8),
                                angle(0, 0, gearRotation)
                        ),
                        noCull()
                );
            }
        }
    }
}
