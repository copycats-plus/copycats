package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.function.Supplier;

public class CopycatStairsModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatStairsModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
        SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));
        // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
        MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
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
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(16, 4, 8).move(0, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(MutableCullFace.UP | MutableCullFace.NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(16, 8, 4).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(16, 8, 4).move(0, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH)
                    );
                }
                case INNER_LEFT -> {
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 8),
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 8).move(0, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(MutableCullFace.UP | MutableCullFace.NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(8, 8, 8).move(8, 8, 8),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(8, 8, 4).move(0, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(8, 8, 4).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 0),
                            aabb(4, 8, 8).move(12, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 0),
                            aabb(4, 8, 8).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 8, 8).move(8, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                }
                case INNER_RIGHT -> {
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 8).move(8, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 8).move(8, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(16, 8, 8).move(0, 0, 8),
                            cull(MutableCullFace.UP | MutableCullFace.NORTH)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(8, 8, 8).move(0, 8, 8),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 12),
                            aabb(8, 8, 4).move(8, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(8, 8, 4).move(8, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 0),
                            aabb(4, 8, 8).move(12, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 0),
                            aabb(4, 8, 8).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 8, 8).move(0, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                }
                case OUTER_LEFT -> {
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 16).move(0, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 16).move(0, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 8).move(8, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 8).move(8, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 8),
                            aabb(8, 8, 8).move(8, 0, 8),
                            cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 12),
                            aabb(4, 8, 4).move(12, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 12),
                            aabb(4, 8, 4).move(0, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(12, 8, 8),
                            aabb(4, 8, 4).move(12, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 8, 8),
                            aabb(4, 8, 4).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                }
                case OUTER_RIGHT -> {
                    assemblePiece(context, facing, top,
                            vec3(8, 0, 0),
                            aabb(8, 4, 16).move(8, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(8, 4, 0),
                            aabb(8, 4, 16).move(8, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 0),
                            aabb(8, 4, 8).move(0, 0, 0),
                            cull(MutableCullFace.UP | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 4, 0),
                            aabb(8, 4, 8).move(0, 12, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 0, 8),
                            aabb(8, 8, 8).move(0, 0, 8),
                            cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 12),
                            aabb(4, 8, 4).move(12, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 12),
                            aabb(4, 8, 4).move(0, 8, 12),
                            cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(4, 8, 8),
                            aabb(4, 8, 4).move(12, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(context, facing, top,
                            vec3(0, 8, 8),
                            aabb(4, 8, 4).move(0, 8, 0),
                            cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                }
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        renderContext.meshConsumer().accept(meshBuilder.build());
    }

}
