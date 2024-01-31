package com.copycatsplus.copycats.content.copycat.layer;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatLayerModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatLayerModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);
        List<BakedQuad> quads = new ArrayList<>();

        int layer = state.getValue(CopycatLayerBlock.LAYERS);
        Direction facing = state.getValue(CopycatLayerBlock.FACING);

        if (facing.getAxis().isVertical()) {
            boolean flipY = facing == Direction.DOWN;
            assemblePiece(
                    templateQuads, quads, 0, flipY,
                    vec3(0, 0, 0),
                    aabb(16, layer, 16),
                    cull(UP)
            );
            assemblePiece(
                    templateQuads, quads, 0, flipY,
                    vec3(0, layer, 0),
                    aabb(16, layer, 16).move(0, 16 - layer, 0),
                    cull(DOWN)
            );
        } else {
            int rot = (int) facing.toYRot();
            assemblePiece(
                    templateQuads, quads, rot, false,
                    vec3(0, 0, 0),
                    aabb(16, 16, layer),
                    cull(SOUTH)
            );
            assemblePiece(
                    templateQuads, quads, rot, false,
                    vec3(0, 0, layer),
                    aabb(16, 16, layer).move(0, 0, 16 - layer),
                    cull(NORTH)
            );
        }


        return quads;
    }
}
