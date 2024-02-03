package com.copycatsplus.copycats.content.copycat.step;

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

public class CopycatHalfModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatHalfModel(BakedModel originalModel) {
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
                        vec3(0, 0, 4),
                        aabb(16, 2, 4).move(0, 0, 4),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(5, 8, 8),
                        aabb(16, 2, 4).move(0, 0, 8),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(8, 8, 8),
                        aabb(16, 1, 4).move(0, 2, 8),
                        cull(0)
                );
                assemblePiece(templateQuads, quads, rot, false,
                        vec3(8, 6, 8),
                        aabb(16, 1, 4).move(0, 2, 4),
                        cull(0)
                );

            }

        }
        return quads;
    }

}