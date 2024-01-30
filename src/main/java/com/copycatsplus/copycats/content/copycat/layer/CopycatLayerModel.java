package com.copycatsplus.copycats.content.copycat.layer;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

public class CopycatLayerModel extends CopycatModel {

    private static final double[] SIZE_BY_LAYER = new double[]{0.0, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0};

    public CopycatLayerModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        BakedModel originalModel = getModelOf(material);
        if (originalModel instanceof CopycatLayerModel impl)
            return impl.originalModel.getQuads(state, side, rand, wrappedData, renderType);
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);
        List<BakedQuad> quads = new ArrayList<>();
        Direction facing = state.getValue(CopycatLayerBlock.FACING);
        for (BakedQuad quad : templateQuads) {
            switch (facing) {
                case UP -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO, 2, SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)], 2), Vec3.ZERO)));
                case DOWN -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO.add(0, 1, 0), 2, SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)], 2), Vec3.ZERO)));
                case WEST -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO.add(1, 0, 1), SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)], 2, 2), Vec3.ZERO)));
                case EAST -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO, SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)], 2, 2), Vec3.ZERO)));
                case SOUTH -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO, 2, 2, SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)]), Vec3.ZERO)));
                case NORTH -> quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad, BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), AABB.ofSize(Vec3.ZERO.add(1, 0, 1), 2, 2, SIZE_BY_LAYER[state.getValue(CopycatLayerBlock.LAYERS)]), Vec3.ZERO)));
            }
        }
        return quads;
    }
}
