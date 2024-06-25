package com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.base.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import com.simibubi.create.AllBlocks;
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
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class MultiStateCopycatModel extends ForwardingBakedModel implements CustomParticleIconModel {

    public MultiStateCopycatModel(BakedModel originalModel) {
        wrapped = originalModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     OcclusionData occlusionData, MultiStateCopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {


            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            BlockState neighbourState = world.getBlockState(neighbourPos);
            if (state.supportsExternalFaceHiding()
                    && neighbourState.hidesNeighborFace(world, neighbourPos, state, face.getOpposite())) {
                occlusionData.occlude(face);
                continue;
            }

            if (!copycatBlock.canFaceBeOccluded(state, face))
                continue;
            if (!Block.shouldRenderFace(material, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        Map<String, BlockState> materials;
        if (blockView instanceof RenderAttachedBlockView attachmentView
                && attachmentView.getBlockEntityRenderAttachment(pos) instanceof Map<?, ?> mats) {
            synchronized (mats) {
                materials = new HashMap<>((Map<? extends String, ? extends BlockState>) mats);
            }
        } else {
            materials = new HashMap<>();
        }
        if (materials.isEmpty()) {
            if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
                for (String property : copycatBlock.storageProperties()) {
                    materials.put(property, AllBlocks.COPYCAT_BASE.getDefaultState());
                }
            }
        }
        for (Map.Entry<String, BlockState> entry : materials.entrySet()) {
            OcclusionData occlusionData = new OcclusionData();
            if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
                gatherOcclusionData(blockView, pos, state, entry.getValue(), occlusionData, copycatBlock);
            }

            CullFaceRemovalData cullFaceRemovalData = new CullFaceRemovalData();
            if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
                for (Direction cullFace : Iterate.directions) {
                    if (copycatBlock.shouldFaceAlwaysRender(state, cullFace)) {
                        cullFaceRemovalData.remove(cullFace);
                    }
                }
            }

            // fabric: If it is the default state do not push transformations, will cause issues with GhostBlockRenderer
            boolean shouldTransform = entry.getValue() != AllBlocks.COPYCAT_BASE.getDefaultState();

            // fabric: need to change the default render material
            if (shouldTransform)
                context.pushTransform(MaterialFixer.create(entry.getValue()));

            BlockAndTintGetter innerBlockView = blockView;
            if (state.getBlock() instanceof MultiStateCopycatBlock copycatBlock) {
                Vec3i inner = copycatBlock.getVectorFromProperty(state, entry.getKey());
                ScaledBlockAndTintGetter scaledWorld = new ScaledBlockAndTintGetter(entry.getKey(), blockView, pos, inner, copycatBlock.vectorScale(state), p -> true);
                innerBlockView = new ScaledBlockAndTintGetter(entry.getKey(), blockView, pos, inner, copycatBlock.vectorScale(state),
                        targetPos -> {
                            BlockEntity be = blockView.getBlockEntity(pos);
                            if (be instanceof MultiStateCopycatBlockEntity mscbe)
                                if (!mscbe.getMaterialItemStorage().getMaterialItem(entry.getKey()).enableCT())
                                    return false;
                            return copycatBlock.canConnectTexturesToward(entry.getKey(), scaledWorld, pos, targetPos, state);
                        });
            }
            emitBlockQuadsInner(entry.getKey(), innerBlockView, state, pos, randomSupplier, context, entry.getValue(), cullFaceRemovalData, occlusionData);

            // fabric: pop the material changer transform
            if (shouldTransform)
                context.popTransform();
        }
    }

    protected abstract void emitBlockQuadsInner(String key, BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData);

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(Object data) {
        if (data instanceof Map<?, ?> mats) {
            if (mats.isEmpty())
                return super.getParticleIcon();
            Map.Entry<String, BlockState> key = (Map.Entry<String, BlockState>) mats.entrySet().stream().findFirst().get();
            return getIcon(getModelOf(key.getValue()), null);
        }
        return CustomParticleIconModel.super.getParticleIcon(data);
    }

    public static TextureAtlasSprite getIcon(BakedModel model, @Nullable Object data) {
        if (model instanceof CustomParticleIconModel particleIconModel)
            return particleIconModel.getParticleIcon(data);
        return model.getParticleIcon();
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }


    //Copied from com.simibubi.create.content.decoration.copycat.CopycatModel.OcclusionData as it was private
    static class OcclusionData {
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

    //Copied from fabric version of create as they were private classes
    protected static class CullFaceRemovalData {
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

    private record MaterialFixer(RenderMaterial materialDefault) implements RenderContext.QuadTransform {
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
            return new MaterialFixer(renderMaterial);
        }
    }
}
