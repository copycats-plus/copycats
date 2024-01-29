package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatTrapdoorModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);

        List<BakedQuad> quads = new ArrayList<>();

        int rot = (int) state.getValue(FACING).toYRot();
        boolean flipY = state.getValue(HALF) == Half.TOP;
        boolean open = state.getValue(OPEN);

        if (!open) {
            assemblePiece(
                    templateQuads, quads, rot, flipY,
                    vec3(0, 0, 0),
                    aabb(16, 1, 16),
                    cull(MutableCullFace.UP)
            );
            assemblePiece(
                    templateQuads, quads, rot, flipY,
                    vec3(0, 1, 0),
                    aabb(16, 2, 16).move(0, 14, 0),
                    cull(MutableCullFace.DOWN)
            );
        } else {
            assemblePiece(
                    templateQuads, quads, rot, flipY,
                    vec3(0, 0, 0),
                    aabb(16, 16, 1),
                    cull(MutableCullFace.SOUTH)
            );
            assemblePiece(
                    templateQuads, quads, rot, flipY,
                    vec3(0, 0, 1),
                    aabb(16, 16, 2).move(0, 0, 14),
                    cull(MutableCullFace.NORTH)
            );
        }

        return quads;
    }

}
