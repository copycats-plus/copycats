package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatButtonModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatButtonModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);
        AttachFace face = state.getValue(ButtonBlock.FACE);
        int rot = (int) state.getValue(ButtonBlock.FACING).toYRot();
        boolean pressed = state.getValue(ButtonBlock.POWERED);

        List<BakedQuad> quads = new ArrayList<>();
        switch (face) {
            case WALL -> {
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(5, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 1, 1),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(5, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(1, 13, 1),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(8, 8, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 13, 1),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(8, 6, (pressed ? 0 : 1)),
                        aabb(3, 2, 1).move(12, 1, 1),
                        cull(0)
                );
                if (!pressed) {
                    assemblePiece(templateQuads, quads, rot, false,
                            vec3(5, 6, 0),
                            aabb(3, 2, 1),
                            cull(SOUTH | NORTH)
                    );
                    assemblePiece(templateQuads, quads, rot, false,
                            vec3(5, 8, 0),
                            aabb(3, 2, 1).move(0, 14, 0),
                            cull(SOUTH | NORTH)
                    );
                    assemblePiece(templateQuads, quads, rot, false,
                            vec3(8, 8, 0),
                            aabb(3, 2, 1).move(13, 14, 0),
                            cull(SOUTH | NORTH)
                    );
                    assemblePiece(templateQuads, quads, rot, false,
                            vec3(8, 6, 0),
                            aabb(3, 2, 1).move(13, 0, 0),
                            cull(SOUTH | NORTH)
                    );
                }
            }
            case CEILING, FLOOR -> {
                assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                        vec3(5, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(1, 0, 1),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                        vec3(5, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(1, 0, 13),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                        vec3(8, (pressed ? 0 : 1), 6),
                        aabb(3, 1, 2).move(12, 0, 1),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                        vec3(8, (pressed ? 0 : 1), 8),
                        aabb(3, 1, 2).move(12, 0, 13),
                        cull(0)
                );
                if (!pressed) {
                    assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                            vec3(5, 0, 6),
                            aabb(3, 1, 2).move(0, 0, 0),
                            cull(UP | DOWN)
                    );
                    assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                            vec3(5, 0, 8),
                            aabb(3, 1, 2).move(0, 0, 14),
                            cull(UP | DOWN)
                    );
                    assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                            vec3(8, 0, 6),
                            aabb(3, 1, 2).move(13, 0, 0),
                            cull(UP | DOWN)
                    );
                    assemblePiece(templateQuads, quads, rot, (face != AttachFace.FLOOR),
                            vec3(8, 0, 8),
                            aabb(3, 1, 2).move(13, 0, 14),
                            cull(UP | DOWN)
                    );
                }
            }
        }
        return quads;
    }
}
