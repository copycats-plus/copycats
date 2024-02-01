package com.copycatsplus.copycats.content.copycat.beam;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

public class CopycatBeamModel extends CopycatModel {
    protected static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    public CopycatBeamModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
        Axis axis = state.getOptionalValue(CopycatBeamBlock.AXIS).orElse(Axis.Y);
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
            List<BakedQuad> templateQuads = model.getQuads(state, quad.lightFace(), randomSupplier.get());
            int size = templateQuads.size();

            Vec3 normal = Vec3.atLowerCornerOf(Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE).getNormal());
            Vec3 rowNormal = axis.isVertical() ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
            Vec3 columnNormal = axis.isVertical() || axis == Axis.X ? new Vec3(0, 0, 1) : new Vec3(1, 0, 0);
            AABB bb = CUBE_AABB.contract((1 - normal.x) * 12 / 16, (1 - normal.y) * 12 / 16, (1 - normal.z) * 12 / 16);

            // 4 Pieces
            for (boolean row : Iterate.trueAndFalse) {
                for (boolean column : Iterate.trueAndFalse) {

                    AABB bb1 = bb;
                    if (row)
                        bb1 = bb1.move(rowNormal.scale(12 / 16.0));
                    if (column)
                        bb1 = bb1.move(columnNormal.scale(12 / 16.0));

                    Vec3 offset = Vec3.ZERO;
                    Vec3 rowShift = rowNormal.scale(row ? -4 / 16.0 : 4 / 16.0);
                    Vec3 columnShift = columnNormal.scale(column ? -4 / 16.0 : 4 / 16.0);
                    offset = offset.add(rowShift);
                    offset = offset.add(columnShift);

                    rowShift = rowShift.normalize();
                    columnShift = columnShift.normalize();
                    Vec3i rowShiftNormal = new Vec3i((int) rowShift.x, (int) rowShift.y, (int) rowShift.z);
                    Vec3i columnShiftNormal = new Vec3i((int) columnShift.x, (int) columnShift.y, (int) columnShift.z);

                    for (int i = 0; i < size; i++) {
                        Direction direction = quad.lightFace();

                        if (rowShiftNormal.equals(direction.getNormal()))
                            continue;
                        if (columnShiftNormal.equals(direction.getNormal()))
                            continue;

                        RenderMaterial quadMaterial = quad.material();
                        quad.copyTo(emitter);
                        emitter.material(quadMaterial);
                        BakedModelHelper.cropAndMove(emitter, spriteFinder.find(emitter), bb1, offset);
                        emitter.emit();
                    }

                }
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, context);
        context.popTransform();
        context.meshConsumer().accept(meshBuilder.build());
    }

}
