package com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric.AssemblerImpl.CopycatRenderContextFabric;
import com.copycatsplus.copycats.content.copycat.base.model.fabric.CopycatModel;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric.AssemblerImpl.assembleQuad;

public class SimpleMultiStateCopycatModel extends MultiStateCopycatModel {

    private final SimpleMultiStateCopycatPart part;
    public SimpleMultiStateCopycatModel(BakedModel originalModel, SimpleMultiStateCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    protected void emitBlockQuadsInner(String key, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);

        // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
        MeshBuilder meshBuilder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        renderContext.pushTransform(quad -> {
            CopycatRenderContextFabric context = new CopycatRenderContextFabric(quad, emitter);
            if (cullFaceRemovalData.shouldRemove(quad.cullFace())) {
                quad.cullFace(null);
            } else if (occlusionData.isOccluded(quad.cullFace())) {
                // Add quad to mesh and do not render original quad to preserve quad render order
                assembleQuad(context);
                return false;
            }

            part.emitCopycatQuads(key, state, context, material);
            return false;
        });
        ((FabricBakedModel) model).emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();

        renderContext.meshConsumer().accept(meshBuilder.build());
    }
}
