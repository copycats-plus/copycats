package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock.byDirection;

public class CopycatBoardModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatBoardModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);
        List<BakedQuad> quads = new ArrayList<>();
        CopycatRenderContext context = context(templateQuads, quads);

        Map<Direction, Boolean> topEdges = new HashMap<>();
        Map<Direction, Boolean> bottomEdges = new HashMap<>();
        Map<Direction, Boolean> leftEdges = new HashMap<>();

        for (Direction direction : Iterate.horizontalDirections) {
            topEdges.put(direction, false);
            bottomEdges.put(direction, false);
            leftEdges.put(direction, false);
        }

        for (Direction direction : Iterate.directions) {
            if (state.getValue(byDirection(direction)))
                if (direction.getAxis().isVertical()) {
                    Map<Direction, Boolean> edges = direction == Direction.DOWN ? bottomEdges : topEdges;
                    int north = !edges.get(Direction.NORTH) ? 1 : 0;
                    int south = !edges.get(Direction.SOUTH) ? 1 : 0;
                    int east = !edges.get(Direction.EAST) ? 1 : 0;
                    int west = !edges.get(Direction.WEST) ? 1 : 0;
                    if (north == 1) edges.put(Direction.NORTH, true);
                    if (south == 1) edges.put(Direction.SOUTH, true);
                    if (east == 1) edges.put(Direction.EAST, true);
                    if (west == 1) edges.put(Direction.WEST, true);
                    assemblePiece(context, 0, direction == Direction.UP,
                            vec3(1 - west, 0, 1 - north),
                            aabb(14 + east + west, 1, 14 + north + south).move(1 - west, 0, 1 - north),
                            cull(MutableCullFace.NORTH * (1 - north) | MutableCullFace.SOUTH * (1 - south) | MutableCullFace.EAST * (1 - east) | MutableCullFace.WEST * (1 - west))
                    );
                } else {
                    int up = !topEdges.get(direction) ? 1 : 0;
                    int down = !bottomEdges.get(direction) ? 1 : 0;
                    int left = !leftEdges.get(direction) ? 1 : 0;
                    int right = !leftEdges.get(direction.getCounterClockWise()) ? 1 : 0;
                    if (up == 1) topEdges.put(direction, true);
                    if (down == 1) bottomEdges.put(direction, true);
                    if (left == 1) leftEdges.put(direction, true);
                    if (right == 1) leftEdges.put(direction.getCounterClockWise(), true);
                    assemblePiece(context, (int) direction.toYRot() + 180, false,
                            vec3(1 - right, 1 - down, 0),
                            aabb(14 + left + right, 14 + up + down, 1).move(1 - right, 1 - down, 0),
                            cull(MutableCullFace.UP * (1 - up) | MutableCullFace.DOWN * (1 - down) | MutableCullFace.EAST * (1 - left) | MutableCullFace.WEST * (1 - right))
                    );
                }
        }

        return quads;
    }

}
