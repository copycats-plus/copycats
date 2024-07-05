package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.forge.AssemblerImpl.CopycatRenderContextForge;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleCopycatModel extends CopycatModel {

    private final SimpleCopycatPart part;

    public SimpleCopycatModel(BakedModel originalModel, SimpleCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    public List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand, BlockState material, IModelData wrappedData) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData);
        List<BakedQuad> quads = new ArrayList<>();
        CopycatRenderContextForge context = new CopycatRenderContextForge(templateQuads, quads);

        part.emitCopycatQuads(state, context, material);

        return quads;
    }
}
