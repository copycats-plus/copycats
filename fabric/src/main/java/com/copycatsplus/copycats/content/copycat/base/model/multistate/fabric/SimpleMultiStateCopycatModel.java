package com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric.AssemblerImpl;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric.AssemblerImpl.CopycatRenderContextFabric;
import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric.AssemblerImpl.assembleQuad;

public class SimpleMultiStateCopycatModel extends MultiStateCopycatModel {

    private final SimpleMultiStateCopycatPart part;
    public SimpleMultiStateCopycatModel(BakedModel originalModel, SimpleMultiStateCopycatPart part) {
        super(originalModel);
        this.part = part;
    }

    @Override
    protected void emitBlockQuadsInner(String key, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
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
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();

        meshBuilder.build().outputTo(renderContext.getEmitter());
    }



/*    @Override
    protected List<BakedQuad> getCroppedQuads(String key, BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        List<BakedQuad> templateQuads = getModelOf(material).getQuads(material, side, rand, wrappedData, renderType);
        QuadHelper.CopycatRenderContext<List<BakedQuad>, List<BakedQuad>> context = new QuadHelper.CopycatRenderContext<>(templateQuads, quads);

        part.emitCopycatQuads(key, state, context, material);

        return quads;
    }*/
}
