package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

public class CopycatStairsModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatStairsModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);

        List<BakedQuad> quads = new ArrayList<>();

        int facing = (int) state.getValue(StairBlock.FACING).toYRot();
        boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
        StairsShape shape = state.getValue(StairBlock.SHAPE);

        switch (shape) {
            case STRAIGHT -> {
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 0),
                        aabb(16, 4, 8),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 4, 0),
                        aabb(16, 4, 8).move(0, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 8),
                        aabb(16, 8, 8).move(0, 0, 8),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 8),
                        aabb(16, 8, 4).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 12),
                        aabb(16, 8, 4).move(0, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH)
                );
            }
            case INNER_LEFT -> {
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 0),
                        aabb(8, 4, 8),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 4, 0),
                        aabb(8, 4, 8).move(0, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 8),
                        aabb(16, 8, 8).move(0, 0, 8),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 8),
                        aabb(8, 8, 8).move(8, 8, 8),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 12),
                        aabb(8, 8, 4).move(0, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 8),
                        aabb(8, 8, 4).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(12, 8, 0),
                        aabb(4, 8, 8).move(12, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 0),
                        aabb(4, 8, 8).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 0, 0),
                        aabb(8, 8, 8).move(8, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
            }
            case INNER_RIGHT -> {
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 0, 0),
                        aabb(8, 4, 8).move(8, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 4, 0),
                        aabb(8, 4, 8).move(8, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 8),
                        aabb(16, 8, 8).move(0, 0, 8),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 8),
                        aabb(8, 8, 8).move(0, 8, 8),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 12),
                        aabb(8, 8, 4).move(8, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 8),
                        aabb(8, 8, 4).move(8, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(4, 8, 0),
                        aabb(4, 8, 8).move(12, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 0),
                        aabb(4, 8, 8).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 0),
                        aabb(8, 8, 8).move(0, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
            }
            case OUTER_LEFT -> {
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 0),
                        aabb(8, 4, 16).move(0, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 4, 0),
                        aabb(8, 4, 16).move(0, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 0, 0),
                        aabb(8, 4, 8).move(8, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 4, 0),
                        aabb(8, 4, 8).move(8, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 0, 8),
                        aabb(8, 8, 8).move(8, 0, 8),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(12, 8, 12),
                        aabb(4, 8, 4).move(12, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 12),
                        aabb(4, 8, 4).move(0, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(12, 8, 8),
                        aabb(4, 8, 4).move(12, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 8, 8),
                        aabb(4, 8, 4).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
            }
            case OUTER_RIGHT -> {
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 0, 0),
                        aabb(8, 4, 16).move(8, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(8, 4, 0),
                        aabb(8, 4, 16).move(8, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 0),
                        aabb(8, 4, 8).move(0, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 4, 0),
                        aabb(8, 4, 8).move(0, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 0, 8),
                        aabb(8, 8, 8).move(0, 0, 8),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(4, 8, 12),
                        aabb(4, 8, 4).move(12, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 12),
                        aabb(4, 8, 4).move(0, 8, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(4, 8, 8),
                        aabb(4, 8, 4).move(12, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(templateQuads, quads, facing, top,
                        vec3(0, 8, 8),
                        aabb(4, 8, 4).move(0, 8, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
            }
        }

        return quads;
    }

}
