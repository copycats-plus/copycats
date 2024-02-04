package com.copycatsplus.copycats.content.copycat.fence;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatFenceModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatFenceModel(BakedModel originalModel) {
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
            for (Direction direction : Iterate.horizontalDirections) {
                assemblePiece(context, (int) direction.toYRot(), false,
                        vec3(6, 0, 6),
                        aabb(2, 16, 2),
                        cull(SOUTH | EAST)
                );
            }

            for (Direction direction : Iterate.horizontalDirections) {
                if (!state.getValue(CopycatFenceBlock.byDirection(direction))) continue;

                int rot = (int) direction.toYRot();
                assemblePiece(context, rot, false,
                        vec3(7, 6, 10),
                        aabb(1, 1, 6),
                        cull(UP | NORTH | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 6, 10),
                        aabb(1, 1, 6).move(15, 0, 0),
                        cull(UP | NORTH | WEST)
                );
                assemblePiece(context, rot, false,
                        vec3(7, 7, 10),
                        aabb(1, 2, 6).move(0, 14, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 7, 10),
                        aabb(1, 2, 6).move(15, 14, 0),
                        cull(DOWN | NORTH | WEST)
                );

                assemblePiece(context, rot, false,
                        vec3(7, 12, 10),
                        aabb(1, 1, 6),
                        cull(UP | NORTH | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 12, 10),
                        aabb(1, 1, 6).move(15, 0, 0),
                        cull(UP | NORTH | WEST)
                );
                assemblePiece(context, rot, false,
                        vec3(7, 13, 10),
                        aabb(1, 2, 6).move(0, 14, 0),
                        cull(DOWN | NORTH | EAST)
                );
                assemblePiece(context, rot, false,
                        vec3(8, 13, 10),
                        aabb(1, 2, 6).move(15, 14, 0),
                        cull(DOWN | NORTH | WEST)
                );
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        meshBuilder.build().outputTo(renderContext.getEmitter());
    }

}
