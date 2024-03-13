package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.CopycatRenderContext;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleCopycatModel extends CopycatModel {

    private SimpleCopycatPart part;

    public SimpleCopycatModel(BakedModel originalModel, SimpleCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand, BlockState material, IModelData wrappedData) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData);
        List<BakedQuad> quads = new ArrayList<>();
        CopycatRenderContext<List<BakedQuad>, List<BakedQuad>> context = new CopycatRenderContext<>(templateQuads, quads);

        part.emitCopycatQuads(state, context, material);

        return quads;
    }
}
