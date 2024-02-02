package com.copycatsplus.copycats.content.copycat.trapdoor;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
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
import net.minecraft.world.level.block.state.properties.Half;

import java.util.function.Supplier;

import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatTrapdoorModel(BakedModel originalModel) {
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
            int rot = (int) state.getValue(FACING).toYRot();
            boolean flipY = state.getValue(HALF) == Half.TOP;
            boolean open = state.getValue(OPEN);

            if (!open) {
                assemblePiece(
                        quad, emitter, rot, flipY,
                        vec3(0, 0, 0),
                        aabb(16, 1, 16),
                        cull(MutableCullFace.UP)
                );
                assemblePiece(
                        quad, emitter, rot, flipY,
                        vec3(0, 1, 0),
                        aabb(16, 2, 16).move(0, 14, 0),
                        cull(MutableCullFace.DOWN)
                );
            } else {
                assemblePiece(
                        quad, emitter, rot, flipY,
                        vec3(0, 0, 0),
                        aabb(16, 16, 1),
                        cull(MutableCullFace.SOUTH)
                );
                assemblePiece(
                        quad, emitter, rot, flipY,
                        vec3(0, 0, 1),
                        aabb(16, 16, 2).move(0, 0, 14),
                        cull(MutableCullFace.NORTH)
                );
            }
            return false;
        });
        ((FabricBakedModel) model).emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }

}
