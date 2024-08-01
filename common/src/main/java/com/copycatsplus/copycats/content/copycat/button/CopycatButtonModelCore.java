package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatButtonModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(ButtonBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof ButtonBlock) {
            context.assembleAll();
            return;
        }

        AttachFace face = state.getValue(ButtonBlock.FACE);
        int rot = (int) state.getValue(ButtonBlock.FACING).toYRot();
        boolean pressed = state.getValue(ButtonBlock.POWERED);
        switch (face) {
            case WALL -> {
                AssemblyTransform transform = t -> t.rotateY(rot);
                context.assemblePiece(
                        transform,
                        vec3(5, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 1, 1),
                        cull(UP | EAST)
                );
                context.assemblePiece(
                        transform,
                        vec3(5, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 13, 1),
                        cull(DOWN | EAST)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 13, 1),
                        cull(DOWN | WEST)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 1, 1),
                        cull(UP | WEST)
                );
                if (!pressed) {
                    context.assemblePiece(
                            transform,
                            vec3(5, 6, 0),
                            aabb(3, 2, 1),
                            cull(SOUTH | UP | EAST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(5, 8, 0),
                            aabb(3, 2, 1).move(0, 14, 0),
                            cull(SOUTH | DOWN | EAST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(8, 8, 0),
                            aabb(3, 2, 1).move(13, 14, 0),
                            cull(SOUTH | DOWN | WEST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(8, 6, 0),
                            aabb(3, 2, 1).move(13, 0, 0),
                            cull(SOUTH | UP | WEST)
                    );
                }
            }
            case CEILING, FLOOR -> {
                AssemblyTransform transform = t -> t.rotateY(rot).flipY(face != AttachFace.FLOOR);
                context.assemblePiece(
                        transform,
                        vec3(5, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(1, 0, 1),
                        cull(SOUTH | EAST)
                );
                context.assemblePiece(
                        transform,
                        vec3(5, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(1, 0, 13),
                        cull(NORTH | EAST)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(12, 0, 1),
                        cull(SOUTH | WEST)
                );
                context.assemblePiece(
                        transform,
                        vec3(8, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(12, 0, 13),
                        cull(NORTH | WEST)
                );
                if (!pressed) {
                    context.assemblePiece(
                            transform,
                            vec3(5, 0, 6),
                            aabb(3, 1, 2).move(0, 0, 0),
                            cull(UP | SOUTH | EAST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(5, 0, 8),
                            aabb(3, 1, 2).move(0, 0, 14),
                            cull(UP | NORTH | EAST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(8, 0, 6),
                            aabb(3, 1, 2).move(13, 0, 0),
                            cull(UP | SOUTH | WEST)
                    );
                    context.assemblePiece(
                            transform,
                            vec3(8, 0, 8),
                            aabb(3, 1, 2).move(13, 0, 14),
                            cull(UP | NORTH | WEST)
                    );
                }
            }
        }
    }
}
