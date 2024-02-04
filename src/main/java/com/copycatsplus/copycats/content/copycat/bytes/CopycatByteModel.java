package com.copycatsplus.copycats.content.copycat.bytes;

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
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CopycatByteModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatByteModel(BakedModel originalModel) {
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
            for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
                if (!state.getValue(CopycatByteBlock.byByte(bite))) continue;

                int offsetX = bite.x() ? 8 : 0;
                int offsetY = bite.y() ? 8 : 0;
                int offsetZ = bite.z() ? 8 : 0;

                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY, offsetZ),
                        aabb(4, 4, 4),
                        cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY, offsetZ),
                        aabb(4, 4, 4).move(12, 0, 0),
                        cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY, offsetZ + 4),
                        aabb(4, 4, 4).move(0, 0, 12),
                        cull(MutableCullFace.UP | MutableCullFace.EAST | MutableCullFace.NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY, offsetZ + 4),
                        aabb(4, 4, 4).move(12, 0, 12),
                        cull(MutableCullFace.UP | MutableCullFace.WEST | MutableCullFace.NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY + 4, offsetZ),
                        aabb(4, 4, 4).move(0, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY + 4, offsetZ),
                        aabb(4, 4, 4).move(12, 12, 0),
                        cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY + 4, offsetZ + 4),
                        aabb(4, 4, 4).move(0, 12, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.EAST | MutableCullFace.NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                        aabb(4, 4, 4).move(12, 12, 12),
                        cull(MutableCullFace.DOWN | MutableCullFace.WEST | MutableCullFace.NORTH)
                );
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        renderContext.meshConsumer().accept(meshBuilder.build());
    }

}
