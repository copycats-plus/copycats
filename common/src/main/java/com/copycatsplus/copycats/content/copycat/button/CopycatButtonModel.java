package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;
import static com.copycatsplus.copycats.content.copycat.base.model.PlatformModelUtils.*;
import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.MutableCullFace.*;

public class CopycatButtonModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext context, BlockState material) {
        AttachFace face = state.getValue(ButtonBlock.FACE);
        int rot = (int) state.getValue(ButtonBlock.FACING).toYRot();
        boolean pressed = state.getValue(ButtonBlock.POWERED);
        switch (face) {
            case WALL -> {
                assemblePiece(context, rot, false,
                        vec3(5, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 1, 1),
                        cull(UP | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(5, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 13, 1),
                        cull(DOWN | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 13, 1),
                        cull(DOWN | WEST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 1, 1),
                        cull(UP | WEST)
                );
                if (!pressed) {
                    assemblePiece(context, rot, false,
                            vec3(5, 6, 0),
                            aabb(3, 2, 1),
                            cull(SOUTH | UP | EAST)
                    );
                    assemblePiece(context, rot, false,
                            vec3(5, 8, 0),
                            aabb(3, 2, 1).move(0, 14, 0),
                            cull(SOUTH | DOWN | EAST)
                    );
                    assemblePiece(context, rot, false,
                            vec3(8, 8, 0),
                            aabb(3, 2, 1).move(13, 14, 0),
                            cull(SOUTH | DOWN | WEST)
                    );
                    assemblePiece(context, rot, false,
                            vec3(8, 6, 0),
                            aabb(3, 2, 1).move(13, 0, 0),
                            cull(SOUTH | UP | WEST)
                    );
                }
            }
            case CEILING, FLOOR -> {
                assemblePiece(context, rot, (face != AttachFace.FLOOR),
                        vec3(5, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(1, 0, 1),
                        cull(SOUTH | EAST)
                );
                assemblePiece(context, rot, (face != AttachFace.FLOOR),
                        vec3(5, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(1, 0, 13),
                        cull(NORTH | EAST)
                );
                assemblePiece(context, rot, (face != AttachFace.FLOOR),
                        vec3(8, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(12, 0, 1),
                        cull(SOUTH | WEST)
                );
                assemblePiece(context, rot, (face != AttachFace.FLOOR),
                        vec3(8, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(12, 0, 13),
                        cull(NORTH | WEST)
                );
                if (!pressed) {
                    assemblePiece(context, rot, (face != AttachFace.FLOOR),
                            vec3(5, 0, 6),
                            aabb(3, 1, 2).move(0, 0, 0),
                            cull(UP | SOUTH | EAST)
                    );
                    assemblePiece(context, rot, (face != AttachFace.FLOOR),
                            vec3(5, 0, 8),
                            aabb(3, 1, 2).move(0, 0, 14),
                            cull(UP | NORTH | EAST)
                    );
                    assemblePiece(context, rot, (face != AttachFace.FLOOR),
                            vec3(8, 0, 6),
                            aabb(3, 1, 2).move(13, 0, 0),
                            cull(UP | SOUTH | WEST)
                    );
                    assemblePiece(context, rot, (face != AttachFace.FLOOR),
                            vec3(8, 0, 8),
                            aabb(3, 1, 2).move(13, 0, 14),
                            cull(UP | NORTH | WEST)
                    );
                }
            }
        }
    }
}
