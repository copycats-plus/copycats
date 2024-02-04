package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.Objects;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatStairsModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatStairsModel(BakedModel originalModel) {
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
            int facing = (int) state.getValue(StairBlock.FACING).toYRot();
            boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
            StairsShape shape = state.getValue(StairBlock.SHAPE);

            switch (shape) {
                case STRAIGHT -> {
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(16, 4, 8),
                            cull(UP | SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(16, 4, 8).move(0, 12, 0),
                            cull(DOWN | SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(UP | NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(16, 8, 4).move(0, 8, 0),
                            cull(DOWN | SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(16, 8, 4).move(0, 8, 12),
                            cull(DOWN | NORTH)
                    );
                }
                case INNER_LEFT -> {
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 8),
                            cull(UP | SOUTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 8).move(0, 12, 0),
                            cull(DOWN | SOUTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(UP | NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(8, 8, 8).move(8, 8, 8),
                            cull(DOWN | NORTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(8, 8, 4).move(0, 8, 12),
                            cull(DOWN | NORTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(8, 8, 4).move(0, 8, 0),
                            cull(DOWN | SOUTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 0),
                            aabb(4, 8, 8).move(12, 8, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 0),
                            aabb(4, 8, 8).move(0, 8, 0),
                            cull(DOWN | SOUTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 8, 8).move(8, 0, 0),
                            cull(UP | SOUTH | WEST)
                    );
                }
                case INNER_RIGHT -> {
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 8).move(8, 0, 0),
                            cull(UP | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 8).move(8, 12, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(UP | NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(8, 8, 8).move(0, 8, 8),
                            cull(DOWN | NORTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 12),
                            aabb(8, 8, 4).move(8, 8, 12),
                            cull(DOWN | NORTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(8, 8, 4).move(8, 8, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 0),
                            aabb(4, 8, 8).move(12, 8, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 0),
                            aabb(4, 8, 8).move(0, 8, 0),
                            cull(DOWN | SOUTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 8, 8).move(0, 0, 0),
                            cull(UP | SOUTH | EAST)
                    );
                }
                case OUTER_LEFT -> {
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 16).move(0, 0, 0),
                            cull(UP | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 16).move(0, 12, 0),
                            cull(DOWN | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 8).move(8, 0, 0),
                            cull(UP | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 8).move(8, 12, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 8),
                            aabb(8, 8, 8).move(8, 0, 8),
                            cull(UP | NORTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 12),
                            aabb(4, 8, 4).move(12, 8, 12),
                            cull(DOWN | NORTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 12),
                            aabb(4, 8, 4).move(0, 8, 12),
                            cull(DOWN | NORTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 8),
                            aabb(4, 8, 4).move(12, 8, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(4, 8, 4).move(0, 8, 0),
                            cull(DOWN | SOUTH | EAST)
                    );
                }
                case OUTER_RIGHT -> {
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 16).move(8, 0, 0),
                            cull(UP | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 16).move(8, 12, 0),
                            cull(DOWN | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 8).move(0, 0, 0),
                            cull(UP | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 8).move(0, 12, 0),
                            cull(DOWN | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(8, 8, 8).move(0, 0, 8),
                            cull(UP | NORTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 12),
                            aabb(4, 8, 4).move(12, 8, 12),
                            cull(DOWN | NORTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(4, 8, 4).move(0, 8, 12),
                            cull(DOWN | NORTH | EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 8),
                            aabb(4, 8, 4).move(12, 8, 0),
                            cull(DOWN | SOUTH | WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(4, 8, 4).move(0, 8, 0),
                            cull(DOWN | SOUTH | EAST)
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
