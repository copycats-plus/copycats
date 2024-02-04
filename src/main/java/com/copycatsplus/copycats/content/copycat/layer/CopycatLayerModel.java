package com.copycatsplus.copycats.content.copycat.layer;

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
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatLayerModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatLayerModel(BakedModel originalModel) {
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
            int layer = state.getValue(CopycatLayerBlock.LAYERS);
            Direction facing = state.getValue(CopycatLayerBlock.FACING);

            if (facing.getAxis().isVertical()) {
                boolean flipY = facing == Direction.DOWN;
                assemblePiece(
                        context, 0, flipY,
                        vec3(0, 0, 0),
                        aabb(16, layer, 16),
                        cull(UP)
                );
                assemblePiece(
                        context, 0, flipY,
                        vec3(0, layer, 0),
                        aabb(16, layer, 16).move(0, 16 - layer, 0),
                        cull(DOWN)
                );
            } else {
                int rot = (int) facing.toYRot();
                assemblePiece(
                        context, rot, false,
                        vec3(0, 0, 0),
                        aabb(16, 16, layer),
                        cull(SOUTH)
                );
                assemblePiece(
                        context, rot, false,
                        vec3(0, 0, layer),
                        aabb(16, 16, layer).move(0, 0, 16 - layer),
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
