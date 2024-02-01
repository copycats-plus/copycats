package com.copycatsplus.copycats.content.copycat.fence;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CopycatFenceModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatFenceModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
        SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));
        // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
        MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();
        context.pushTransform(quad -> {
                    if (cullFaceRemovalData.shouldRemove(quad.cullFace())) {
                        quad.cullFace(null);
                    } else if (occlusionData.isOccluded(quad.cullFace())) {
                        // Add quad to mesh and do not render original quad to preserve quad render order
                        // copyTo does not copy the material
                        RenderMaterial quadMaterial = quad.material();
                        quad.copyTo(emitter);
                        emitter.material(quadMaterial);
                        emitter.emit();
                        return false;
                    }
                    for (Direction direction : Iterate.horizontalDirections) {
                        assemblePiece(quad, emitter, (int) direction.toYRot(), false,
                                vec3(6, 0, 6),
                                aabb(2, 16, 2),
                                cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                        );
                    }

                    for (Direction direction : Iterate.horizontalDirections) {
                        if (!state.getValue(CopycatFenceBlock.byDirection(direction))) continue;

                        int rot = (int) direction.toYRot();
                        assemblePiece(quad, emitter, rot, false,
                                vec3(7, 6, 10),
                                aabb(1, 1, 6),
                                cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(8, 6, 10),
                                aabb(1, 1, 6).move(15, 0, 0),
                                cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(7, 7, 10),
                                aabb(1, 2, 6).move(0, 14, 0),
                                cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(8, 7, 10),
                                aabb(1, 2, 6).move(15, 14, 0),
                                cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                        );

                        assemblePiece(quad, emitter, rot, false,
                                vec3(7, 12, 10),
                                aabb(1, 1, 6),
                                cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(8, 12, 10),
                                aabb(1, 1, 6).move(15, 0, 0),
                                cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(7, 13, 10),
                                aabb(1, 2, 6).move(0, 14, 0),
                                cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                        );
                        assemblePiece(quad, emitter, rot, false,
                                vec3(8, 13, 10),
                                aabb(1, 2, 6).move(15, 14, 0),
                                cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                        );
                    }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }

/*    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material,
                                              ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData, renderType);

        List<BakedQuad> quads = new ArrayList<>();

        for (Direction direction : Iterate.horizontalDirections) {
            assemblePiece(templateQuads, quads, (int) direction.toYRot(), false,
                    vec3(6, 0, 6),
                    aabb(2, 16, 2),
                    cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
            );
        }

        for (Direction direction : Iterate.horizontalDirections) {
            if (!state.getValue(CopycatFenceBlock.byDirection(direction))) continue;

            int rot = (int) direction.toYRot();
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(7, 6, 10),
                    aabb(1, 1, 6),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(8, 6, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(7, 7, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(8, 7, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
            );

            assemblePiece(templateQuads, quads, rot, false,
                    vec3(7, 12, 10),
                    aabb(1, 1, 6),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(8, 12, 10),
                    aabb(1, 1, 6).move(15, 0, 0),
                    cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(7, 13, 10),
                    aabb(1, 2, 6).move(0, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
            );
            assemblePiece(templateQuads, quads, rot, false,
                    vec3(8, 13, 10),
                    aabb(1, 2, 6).move(15, 14, 0),
                    cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
            );
        }

        return quads;
    }*/

}
