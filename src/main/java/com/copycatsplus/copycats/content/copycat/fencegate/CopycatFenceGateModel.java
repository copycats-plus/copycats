package com.copycatsplus.copycats.content.copycat.fencegate;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.FenceGateBlock.*;

public class CopycatFenceGateModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatFenceGateModel(BakedModel originalModel) {
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
            int offsetWall = state.getValue(IN_WALL) ? -3 : 0;
            int rot = (int) state.getValue(FACING).toYRot();

            // Assemble the poles
            for (boolean eastSide : Iterate.falseAndTrue) {
                int offsetX = eastSide ? 14 : 0;
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX, 5 + offsetWall, 7),
                        aabb(1, 6, 1),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX + 1, 5 + offsetWall, 7),
                        aabb(1, 6, 1).move(15, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX, 5 + offsetWall, 8),
                        aabb(1, 6, 1).move(0, 0, 15),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX + 1, 5 + offsetWall, 8),
                        aabb(1, 6, 1).move(15, 0, 15),
                        cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX, 11 + offsetWall, 7),
                        aabb(1, 5, 1).move(0, 11, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX + 1, 11 + offsetWall, 7),
                        aabb(1, 5, 1).move(15, 11, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX, 11 + offsetWall, 8),
                        aabb(1, 5, 1).move(0, 11, 15),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.EAST)
                );
                assemblePiece(
                        quad, emitter, rot, false,
                        vec3(offsetX + 1, 11 + offsetWall, 8),
                        aabb(1, 5, 1).move(15, 11, 15),
                        cull(MutableCullFace.DOWN | MutableCullFace.NORTH | MutableCullFace.WEST)
                );
            }

            if (state.getValue(OPEN)) {
                for (boolean eastDoor : Iterate.falseAndTrue) {
                    for (boolean eastSide : Iterate.falseAndTrue) {
                        int offsetX = (eastDoor ? 14 : 0) + (eastSide ? 1 : 0);
                        assemblePiece(
                                quad, emitter, rot, false,
                                vec3(offsetX, 12 + offsetWall, 9),
                                aabb(1, 3, 6).move(eastSide ? 15 : 0, 13, 10),
                                cull(MutableCullFace.NORTH | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST))
                        );
                        assemblePiece(
                                quad, emitter, rot, false,
                                vec3(offsetX, 9 + offsetWall, 13),
                                aabb(1, 3, 2).move(eastSide ? 15 : 0, 7, 14),
                                cull(MutableCullFace.UP | MutableCullFace.DOWN | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST))
                        );
                        assemblePiece(
                                quad, emitter, rot, false,
                                vec3(offsetX, 6 + offsetWall, 9),
                                aabb(1, 3, 6).move(eastSide ? 15 : 0, 0, 10),
                                cull(MutableCullFace.NORTH | (eastSide ? MutableCullFace.WEST : MutableCullFace.EAST))
                        );
                    }
                }
            } else {
                for (boolean southSide : Iterate.falseAndTrue) {
                    int rot2 = rot + (southSide ? 180 : 0);
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(8, 12 + offsetWall, 7),
                            aabb(6, 3, 1).move(0, 13, 0),
                            cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST)
                    );
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(8, 9 + offsetWall, 7),
                            aabb(2, 3, 1).move(0, 7, 0),
                            cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(8, 6 + offsetWall, 7),
                            aabb(6, 3, 1),
                            cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST)
                    );
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(2, 12 + offsetWall, 7),
                            aabb(6, 3, 1).move(10, 13, 0),
                            cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST)
                    );
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(6, 9 + offsetWall, 7),
                            aabb(2, 3, 1).move(14, 7, 0),
                            cull(MutableCullFace.UP | MutableCullFace.DOWN | MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    assemblePiece(
                            quad, emitter, rot2, false,
                            vec3(2, 6 + offsetWall, 7),
                            aabb(6, 3, 1).move(10, 0, 0),
                            cull(MutableCullFace.SOUTH | MutableCullFace.EAST | MutableCullFace.WEST)
                    );
                }
            }
            return false;
        });
        ((FabricBakedModel) model).emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }


}
