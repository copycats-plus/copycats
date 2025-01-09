package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatLargeCogWheelModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(CogWheelBlock.class), EntryType.KINETIC_COPYCAT));
        entries.add(new ModelEntry("cogwheel", ModelGetter.MATERIAL, this, updatePropertiesIfMatch(CogWheelBlock.class), EntryType.KINETIC_COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        Direction.Axis axis = state.getValue(CopycatShaftBlock.AXIS);

        if (material.getBlock() instanceof CogWheelBlock) {
            context.assemblePiece(
                    t -> t.rotateX(axis == Direction.Axis.Z ? 90 : 0).rotateZ(axis == Direction.Axis.X ? 90 : 0),
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
            AssemblyTransform transform = t -> t.rotateZ(rotation).rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
            context.assemblePiece(
                    transform,
                    vec3(1, -1, 5.975),
                    aabb(7, 2, 2.025),
                    cull(EAST | SOUTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(8, -1, 5.975),
                    aabb(7, 2, 2.025).move(9, 0, 0),
                    cull(WEST | SOUTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(1, -1, 8),
                    aabb(7, 2, 2.025).move(0, 0, 13.975),
                    cull(EAST | NORTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(8, -1, 8),
                    aabb(7, 2, 2.025).move(9, 0, 13.975),
                    cull(WEST | NORTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(1, 1, 5.975),
                    aabb(7, 7, 4.05).move(3, 3, 0),
                    cull(EAST | WEST | UP | DOWN),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(-2, -2, 6.4),
                    aabb(10, 10, 1.6),
                    cull(EAST | SOUTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(-2, -2, 8),
                    aabb(10, 10, 1.6).move(0, 0, 14.4),
                    cull(EAST | NORTH | UP),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(-2, -2, 6.625),
                    aabb(10, 10, 1.375),
                    cull(EAST | SOUTH | UP),
                    rotate(
                            pivot(8, 8, 8),
                            angle(0, 0, 45)
                    ),
                    noCull()
            );
            context.assemblePiece(
                    transform,
                    vec3(-2, -2, 8),
                    aabb(10, 10, 1.375).move(0, 0, 14.625),
                    cull(EAST | NORTH | UP),
                    rotate(
                            pivot(8, 8, 8),
                            angle(0, 0, 45)
                    ),
                    noCull()
            );
        }

        for (int i = 0; i < 4; i++) {
            int rotation = i * 90;
            AssemblyTransform transform = t -> t.rotateZ(rotation).rotateY(axis == Direction.Axis.X ? 90 : 0).rotateX(axis == Direction.Axis.Y ? 90 : 0);
            for (int j = 0; j < 4; j++) {
                double gearRotation = j * 22.5;
                double delta = -0.025 + j * 0.025;
                context.assemblePiece(
                        transform,
                        vec3(6.5, -7, 6.6 + delta),
                        aabb(1.5, 6, 1.5),
                        cull(EAST | SOUTH | UP),
                        rotate(
                                pivot(8, 8, 8),
                                angle(0, 0, gearRotation)
                        ),
                        noCull()
                );
                context.assemblePiece(
                        transform,
                        vec3(6.5, -7, 8 + delta),
                        aabb(1.5, 6, 1.5).move(0, 0, 14.5),
                        cull(EAST | NORTH | UP),
                        rotate(
                                pivot(8, 8, 8),
                                angle(0, 0, gearRotation)
                        ),
                        noCull()
                );
                context.assemblePiece(
                        transform,
                        vec3(8, -7, 6.6 + delta),
                        aabb(1.5, 6, 1.5).move(14.5, 0, 0),
                        cull(WEST | SOUTH | UP),
                        rotate(
                                pivot(8, 8, 8),
                                angle(0, 0, gearRotation)
                        ),
                        noCull()
                );
                context.assemblePiece(
                        transform,
                        vec3(8, -7, 8 + delta),
                        aabb(1.5, 6, 1.5).move(14.5, 0, 14.5),
                        cull(WEST | NORTH | UP),
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
