package com.copycatsplus.copycats.content.copycat.trapdoor;

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
import net.minecraft.world.level.block.state.properties.Half;

import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;
import static net.minecraft.world.level.block.TrapDoorBlock.*;

public class CopycatTrapdoorModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatTrapdoorModel(BakedModel originalModel) {
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
            int rot = (int) state.getValue(FACING).toYRot();
            boolean flipY = state.getValue(HALF) == Half.TOP;
            boolean open = state.getValue(OPEN);

            if (!open) {
                assemblePiece(
                        context, rot, flipY,
                        vec3(0, 0, 0),
                        aabb(16, 1, 16),
                        cull(UP)
                );
                assemblePiece(
                        context, rot, flipY,
                        vec3(0, 1, 0),
                        aabb(16, 2, 16).move(0, 14, 0),
                        cull(DOWN)
                );
            } else {
                assemblePiece(
                        context, rot, flipY,
                        vec3(0, 0, 0),
                        aabb(16, 16, 1),
                        cull(SOUTH)
                );
                assemblePiece(
                        context, rot, flipY,
                        vec3(0, 0, 1),
                        aabb(16, 16, 2).move(0, 0, 14),
                        cull(NORTH)
                );
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        meshBuilder.build().outputTo(renderContext.getEmitter());
    }

}
