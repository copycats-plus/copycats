package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class SimpleCopycatModel extends CopycatModel implements ISimpleCopycatModel {
    public SimpleCopycatModel(BakedModel originalModel) {
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

            emitCopycatQuads(blockView, state, pos, context, material);
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();

        meshBuilder.build().outputTo(renderContext.getEmitter());
    }

    protected abstract void emitCopycatQuads(BlockAndTintGetter world, BlockState state, BlockPos pos, CopycatRenderContext context, BlockState material);
}
