package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock.byDirection;

public class CopycatBoardModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatBoardModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
        // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
        MeshBuilder meshBuilder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        renderContext.pushTransform(quad -> {
            CopycatRenderContext context = context(quad, emitter);
            if (cullFaceRemovalData.shouldRemove(quad.cullFace())) {
                quad.cullFace(null);
            } else if (occlusionData.isOccluded(quad.cullFace())) {
                // Add quad to mesh and do not render original quad to preserve quad render order
                assembleQuad(context);
                return false;
            }
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
                                cull(NORTH * (1 - north) | SOUTH * (1 - south) | EAST * (1 - east) | WEST * (1 - west))
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
                                cull(UP * (1 - up) | DOWN * (1 - down) | EAST * (1 - left) | WEST * (1 - right))
                        );
                    }
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        meshBuilder.build().outputTo(renderContext.getEmitter());
    }

}
