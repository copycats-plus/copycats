package com.copycatsplus.copycats.content.copycat.slab;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class CopycatSlabModel extends CopycatModel {

    protected static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);
    private static final SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    public CopycatSlabModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
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
            List<BakedQuad> templateQuads = model.getQuads(state, quad.lightFace(), randomSupplier.get());
            Direction facing = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).isPresent() ? CopycatSlabBlock.getApparentDirection(state) : Direction.UP;
            boolean isDouble = state.getOptionalValue(CopycatSlabBlock.SLAB_TYPE).orElse(SlabType.BOTTOM) == SlabType.DOUBLE;

            // 2 pieces
            for (boolean front : Iterate.trueAndFalse) {
                assemblePiece(facing, quad, emitter, templateQuads, front, false, isDouble);
            }

            // 2 more pieces for double slabs
            if (isDouble) {
                for (boolean front : Iterate.trueAndFalse) {
                    assemblePiece(facing, quad, emitter, templateQuads, front, true, isDouble);
                }
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }

    private static void assemblePiece(Direction facing, MutableQuadView quad, QuadEmitter emitter, List<BakedQuad> templateQuads, boolean front, boolean topSlab, boolean isDouble) {
        int size = templateQuads.size();
        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled12 = normal.scale(12 / 16f);
        Vec3 normalScaledN8 = topSlab ? normal.scale((front ? 0 : -8) / 16f) : normal.scale((front ? 8 : 0) / 16f);
        float contract = 12;
        AABB bb = CUBE_AABB.contract(normal.x * contract / 16, normal.y * contract / 16, normal.z * contract / 16);
        if (!front)
            bb = bb.move(normalScaled12);

        for (int i = 0; i < size; i++) {
            BakedQuad bakedQuad = templateQuads.get(i);
            Direction direction = quad.lightFace();

            if (front && direction == facing)
                continue;
            if (!front && direction == facing.getOpposite())
                continue;
            if (isDouble && topSlab && direction == facing)
                continue;
            if (isDouble && !topSlab && direction == facing.getOpposite())
                continue;

            RenderMaterial quadMaterial = quad.material();
            quad.copyTo(emitter);
            emitter.material(quadMaterial);
            BakedModelHelper.cropAndMove(emitter, spriteFinder.find(emitter), bb, normalScaledN8);
            emitter.emit();

        }
    }
}
