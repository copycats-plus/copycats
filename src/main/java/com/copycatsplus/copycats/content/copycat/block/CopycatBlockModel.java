package com.copycatsplus.copycats.content.copycat.block;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CopycatBlockModel extends CopycatModel {

    public CopycatBlockModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand, BlockState material,
                                              IModelData wrappedData) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData);

        List<BakedQuad> quads = new ArrayList<>();

        for (BakedQuad quad : templateQuads) {
            quads.add(BakedQuadHelper.clone(quad));
        }

        return quads;
    }
}
