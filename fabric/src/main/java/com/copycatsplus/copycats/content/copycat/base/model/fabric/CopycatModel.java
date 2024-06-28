package com.copycatsplus.copycats.content.copycat.base.model.fabric;


import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class CopycatModel extends ForwardingBakedModel implements CustomParticleIconModel {

    public CopycatModel(BakedModel originalModel) {
        wrapped = originalModel;
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     OcclusionData occlusionData, IFunctionalCopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {
            if (!copycatBlock.canFaceBeOccluded(state, face))
                continue;
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            if (!Block.shouldRenderFace(material, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        BlockState material;
        if (blockView instanceof RenderAttachedBlockView attachmentView
                && attachmentView.getBlockEntityRenderAttachment(pos) instanceof BlockState material1) {
            material = material1;
        } else {
            material = AllBlocks.COPYCAT_BASE.getDefaultState();
        }

        OcclusionData occlusionData = new OcclusionData();
        if (state.getBlock() instanceof IFunctionalCopycatBlock copycatBlock) {
            gatherOcclusionData(blockView, pos, state, material, occlusionData, copycatBlock);
        }

        CullFaceRemovalData cullFaceRemovalData = new CullFaceRemovalData();
        if (state.getBlock() instanceof IFunctionalCopycatBlock copycatBlock) {
            for (Direction cullFace : Iterate.directions) {
                if (copycatBlock.shouldFaceAlwaysRender(state, cullFace)) {
                    cullFaceRemovalData.remove(cullFace);
                }
            }
        }

        // fabric: need to change the default render material
        context.pushTransform(MaterialFixer.create(material));

        if (state.getBlock() instanceof IFunctionalCopycatBlock copycatBlock) {
            FilteredBlockAndTintGetter filteredBlockAndTintGetter = new FilteredBlockAndTintGetter(blockView, t -> {
                BlockEntity be = blockView.getBlockEntity(pos);
                if (be instanceof CTCopycatBlockEntity ctbe)
                    if (!ctbe.isCTEnabled())
                        return false;
                return copycatBlock.canConnectTexturesToward(blockView, pos, t, state);
            });
            emitBlockQuadsInner(filteredBlockAndTintGetter, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        } else {
            emitBlockQuadsInner(blockView, state, pos, randomSupplier, context, material, cullFaceRemovalData, occlusionData);
        }

        // fabric: pop the material changer transform
        context.popTransform();
    }

    protected abstract void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData);

    @Override
    public TextureAtlasSprite getParticleIcon(Object data) {
        if (data instanceof BlockState state) {
            BlockState material = getMaterial(state);

            return getIcon(getModelOf(material), null);
        }

        return CustomParticleIconModel.super.getParticleIcon(data);
    }

    public static TextureAtlasSprite getIcon(BakedModel model, @Nullable Object data) {
        if (model instanceof CustomParticleIconModel particleIconModel)
            return particleIconModel.getParticleIcon(data);
        return model.getParticleIcon();
    }

    @Nullable
    public static BlockState getMaterial(BlockState material) {
        return material == null ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }

    public static class OcclusionData {
        private final boolean[] occluded;

        public OcclusionData() {
            occluded = new boolean[6];
        }

        public void occlude(Direction face) {
            occluded[face.get3DDataValue()] = true;
        }

        public boolean isOccluded(Direction face) {
            return face == null ? false : occluded[face.get3DDataValue()];
        }
    }

    public static class CullFaceRemovalData {
        private final boolean[] shouldRemove;

        public CullFaceRemovalData() {
            shouldRemove = new boolean[6];
        }

        public void remove(Direction face) {
            shouldRemove[face.get3DDataValue()] = true;
        }

        public boolean shouldRemove(Direction face) {
            return face == null ? false : shouldRemove[face.get3DDataValue()];
        }
    }

    public record MaterialFixer(RenderMaterial materialDefault) implements RenderContext.QuadTransform {
        @Override
        public boolean transform(MutableQuadView quad) {
            if (quad.material().blendMode() == BlendMode.DEFAULT) {
                // default needs to be changed from the Copycat's default (cutout) to the wrapped material's default.
                quad.material(materialDefault);
            }
            return true;
        }

        public static MaterialFixer create(BlockState materialState) {
            RenderType type = ItemBlockRenderTypes.getChunkRenderType(materialState);
            BlendMode blendMode = BlendMode.fromRenderLayer(type);
            MaterialFinder finder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).materialFinder();
            RenderMaterial renderMaterial = finder.blendMode(0, blendMode).find();
            return new CopycatModel.MaterialFixer(renderMaterial);
        }
    }
}

