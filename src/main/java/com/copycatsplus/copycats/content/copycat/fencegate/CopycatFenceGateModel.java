package com.copycatsplus.copycats.content.copycat.fencegate;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
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
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static net.minecraft.world.level.block.FenceGateBlock.*;

public class CopycatFenceGateModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatFenceGateModel(BakedModel originalModel) {
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
            int offsetWall = state.getValue(IN_WALL) ? -3 : 0;
            int rot = (int) state.getValue(FACING).toYRot();

            // Assemble the poles
            for (boolean eastSide : Iterate.falseAndTrue) {
                int offsetX = eastSide ? 14 : 0;
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX, 5 + offsetWall, 7),
                        aabb(1, 6, 1),
                        cull(UP | SOUTH | EAST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX + 1, 5 + offsetWall, 7),
                        aabb(1, 6, 1).move(15, 0, 0),
                        cull(UP | SOUTH | WEST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX, 5 + offsetWall, 8),
                        aabb(1, 6, 1).move(0, 0, 15),
                        cull(UP | NORTH | EAST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX + 1, 5 + offsetWall, 8),
                        aabb(1, 6, 1).move(15, 0, 15),
                        cull(UP | NORTH | WEST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX, 11 + offsetWall, 7),
                        aabb(1, 5, 1).move(0, 11, 0),
                        cull(DOWN | SOUTH | EAST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX + 1, 11 + offsetWall, 7),
                        aabb(1, 5, 1).move(15, 11, 0),
                        cull(DOWN | SOUTH | WEST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX, 11 + offsetWall, 8),
                        aabb(1, 5, 1).move(0, 11, 15),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(offsetX + 1, 11 + offsetWall, 8),
                        aabb(1, 5, 1).move(15, 11, 15),
                        cull(DOWN | NORTH | WEST)
                );
            }

            if (state.getValue(OPEN)) {
                for (boolean eastDoor : Iterate.falseAndTrue) {
                    for (boolean eastSide : Iterate.falseAndTrue) {
                        int offsetX = (eastDoor ? 14 : 0) + (eastSide ? 1 : 0);
                        assemblePiece(
                                context, rot, false,
                                vec3(offsetX, 12 + offsetWall, 9),
                                aabb(1, 3, 6).move(eastSide ? 15 : 0, 13, 10),
                                cull(NORTH | (eastSide ? WEST : EAST))
                        );
                        assemblePiece(
                                context, rot, false,
                                vec3(offsetX, 9 + offsetWall, 13),
                                aabb(1, 3, 2).move(eastSide ? 15 : 0, 7, 14),
                                cull(UP | DOWN | (eastSide ? WEST : EAST))
                        );
                        assemblePiece(
                                context, rot, false,
                                vec3(offsetX, 6 + offsetWall, 9),
                                aabb(1, 3, 6).move(eastSide ? 15 : 0, 0, 10),
                                cull(NORTH | (eastSide ? WEST : EAST))
                        );
                    }
                }
            } else {
                for (boolean southSide : Iterate.falseAndTrue) {
                    int rot2 = rot + (southSide ? 180 : 0);
                    assemblePiece(
                            context, rot2, false,
                            vec3(8, 12 + offsetWall, 7),
                            aabb(6, 3, 1).move(0, 13, 0),
                            cull(SOUTH | EAST | WEST)
                    );
                    assemblePiece(
                            context, rot2, false,
                            vec3(8, 9 + offsetWall, 7),
                            aabb(2, 3, 1).move(0, 7, 0),
                            cull(UP | DOWN | SOUTH | WEST)
                    );
                    assemblePiece(
                            context, rot2, false,
                            vec3(8, 6 + offsetWall, 7),
                            aabb(6, 3, 1),
                            cull(SOUTH | EAST | WEST)
                    );
                    assemblePiece(
                            context, rot2, false,
                            vec3(2, 12 + offsetWall, 7),
                            aabb(6, 3, 1).move(10, 13, 0),
                            cull(SOUTH | EAST | WEST)
                    );
                    assemblePiece(
                            context, rot2, false,
                            vec3(6, 9 + offsetWall, 7),
                            aabb(2, 3, 1).move(14, 7, 0),
                            cull(UP | DOWN | SOUTH | EAST)
                    );
                    assemblePiece(
                            context, rot2, false,
                            vec3(2, 6 + offsetWall, 7),
                            aabb(6, 3, 1).move(10, 0, 0),
                            cull(SOUTH | EAST | WEST)
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
