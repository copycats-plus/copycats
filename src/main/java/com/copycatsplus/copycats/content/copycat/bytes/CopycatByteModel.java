package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
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

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatByteModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatByteModel(BakedModel originalModel) {
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
            for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
                if (!state.getValue(CopycatByteBlock.byByte(bite))) continue;

                int offsetX = bite.x() ? 8 : 0;
                int offsetY = bite.y() ? 8 : 0;
                int offsetZ = bite.z() ? 8 : 0;

                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY, offsetZ),
                        aabb(4, 4, 4),
                        cull(UP | EAST | SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY, offsetZ),
                        aabb(4, 4, 4).move(12, 0, 0),
                        cull(UP | WEST | SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY, offsetZ + 4),
                        aabb(4, 4, 4).move(0, 0, 12),
                        cull(UP | EAST | NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY, offsetZ + 4),
                        aabb(4, 4, 4).move(12, 0, 12),
                        cull(UP | WEST | NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY + 4, offsetZ),
                        aabb(4, 4, 4).move(0, 12, 0),
                        cull(DOWN | EAST | SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY + 4, offsetZ),
                        aabb(4, 4, 4).move(12, 12, 0),
                        cull(DOWN | WEST | SOUTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX, offsetY + 4, offsetZ + 4),
                        aabb(4, 4, 4).move(0, 12, 12),
                        cull(DOWN | EAST | NORTH)
                );
                assemblePiece(
                        context, 0, false,
                        vec3(offsetX + 4, offsetY + 4, offsetZ + 4),
                        aabb(4, 4, 4).move(12, 12, 12),
                        cull(DOWN | WEST | NORTH)
                );
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        meshBuilder.build().outputTo(renderContext.getEmitter());
    }

}
