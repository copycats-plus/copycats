package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.fabric.CopycatRenderContextFabric;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.MaterialFinder;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
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

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.CCBlockStateProperties.BASE_TYPE;
import static com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock.BASE_TYPE_COUNT;
import static com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore.MATERIAL_KEY;
import static com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore.getModelOf;

public class CopycatModelFabric extends ForwardingBakedModel implements CustomParticleIconModel {

    protected final CopycatModelCore core;
    protected final List<CopycatModelCore.ModelEntry> entries = new ArrayList<>();
    private final boolean disableAO;

    public CopycatModelFabric(BakedModel originalModel, CopycatModelCore core, boolean disableAO) {
        this.disableAO = disableAO;
        this.wrapped = originalModel;
        this.core = core;
        core.registerModels(entries);
    }

    @Override
    public boolean isCustomRenderer() {
        return true; // Stops Continuity from trying to wrap this model
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     OcclusionData occlusionData, ICopycatBlock copycatBlock) {
        if (VirtualEmptyBlockGetter.is(world))
            return;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            if (!Block.shouldRenderFace(state, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return !disableAO && super.useAmbientOcclusion();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        Map<String, BlockState> materials;
        Map<String, Object> remainingDataMap;
        if (blockView instanceof RenderAttachedBlockView attachmentView) {
            Object attachment = attachmentView.getBlockEntityRenderAttachment(pos);
            if (attachment instanceof BlockState material1) {
                materials = Map.of(MATERIAL_KEY, material1);
                remainingDataMap = Map.of();
            } else if (attachment instanceof Pair<?, ?> pair && pair.getSecond() instanceof BlockState material2) {
                materials = Map.of(MATERIAL_KEY, material2);
                if (pair.getFirst() != null)
                    remainingDataMap = Map.of(MATERIAL_KEY, pair.getFirst());
                else
                    remainingDataMap = Map.of();
            } else if (attachment instanceof Map<?, ?> mats) {
                synchronized (attachment) {
                    materials = new HashMap<>();
                    remainingDataMap = new HashMap<>();
                    for (Map.Entry<?, ?> entry : mats.entrySet()) {
                        if (entry.getValue() instanceof Pair<?, ?> pair && pair.getSecond() instanceof BlockState material3) {
                            materials.put((String) entry.getKey(), material3);
                            remainingDataMap.put((String) entry.getKey(), pair.getFirst());
                        } else if (entry.getValue() instanceof BlockState material4) {
                            materials.put((String) entry.getKey(), material4);
                        }
                    }
                }
            } else {
                materials = new HashMap<>();
                remainingDataMap = new HashMap<>();
            }
        } else {
            materials = new HashMap<>();
            remainingDataMap = new HashMap<>();
        }
        final boolean isVirtual = VirtualEmptyBlockGetter.is(blockView);

        for (CopycatModelCore.ModelEntry entry : entries) {
            BlockState material = entry.materialMapper().map(state, materials.get(entry.key()));

            if (material == null && entry.type().useCopycatLogic()) {
                // Don't skip rendering if the world is empty since we might be rendering a placement helper
                if (materials.isEmpty() && isVirtual) {
                    material = AllBlocks.COPYCAT_BASE.getDefaultState();
                } else continue;
            }
            if (entry.type().onlyWhenVirtual() && !isVirtual)
                continue;

            Object remainingData = remainingDataMap.get(entry.key());
            prepareModelCore(blockView, state, pos, randomSupplier, material, remainingData);
            if (entry.type().useCopycatLogic()) {
                OcclusionData occlusionData = new OcclusionData();

                // fabric: If it is the default state do not push transformations, will cause issues with GhostBlockRenderer
                boolean shouldTransform = !material.equals(AllBlocks.COPYCAT_BASE.getDefaultState());
                // fabric: need to change the default render material
                if (shouldTransform)
                    context.pushTransform(MaterialFixer.create(material));

                BlockAndTintGetter renderWorld;
                if (state.getBlock() instanceof IMultiStateCopycatBlock multiStateBlock) {
                    Vec3i inner = multiStateBlock.getVectorFromProperty(state, entry.key());
                    boolean enableCT = !(blockView.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity multiStateBE) || multiStateBE.isCTEnabled();
                    ScaledBlockAndTintGetter scaledWorld = ScaledBlockAndTintGetterFabric.create(isVirtual, entry.key(), remainingData, blockView, pos, inner, multiStateBlock.vectorScale(state), p -> true);
                    renderWorld = ScaledBlockAndTintGetterFabric.create(isVirtual, entry.key(), remainingData, blockView, pos, inner, multiStateBlock.vectorScale(state),
                            targetPos -> {
                                if (!enableCT) return false;
                                return multiStateBlock.canConnectTexturesToward(entry.key(), scaledWorld, pos, targetPos, state);
                            });
                    gatherOcclusionData(scaledWorld, pos, state, material, occlusionData, multiStateBlock);
                } else if (state.getBlock() instanceof ICopycatBlock copycatBlock) {
                    gatherOcclusionData(blockView, pos, state, material, occlusionData, copycatBlock);
                    renderWorld = FilteredBlockAndTintGetterFabric.create(isVirtual, remainingData, blockView, pos, t -> {
                        BlockEntity be = blockView.getBlockEntity(pos);
                        if (be instanceof ICopycatBlockEntity ctbe)
                            if (!ctbe.isCTEnabled())
                                return false;
                        return copycatBlock.canConnectTexturesToward(blockView, pos, t, state);
                    });
                } else {
                    renderWorld = WorldWithRenderData.create(isVirtual, blockView, remainingData, pos);
                }

                BakedModel model = getModelForEntry(entry, state, material);
                if (model == null) continue;

                // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
                MeshBuilder meshBuilder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).meshBuilder();
                QuadEmitter emitter = meshBuilder.getEmitter();

                List<MutableQuadView> quads = new ArrayList<>();

                context.pushTransform(quad -> {
                    if (entry.part() == null) {
                        emitter.copyFrom(quad);
                        emitter.emit();
                    } else {
                        MutableQuadView newQuad = IntermediateMutableQuadView.create();
                        newQuad.copyFrom(quad);
                        quads.add(newQuad);
                    }
                    return false;
                });
                model.emitBlockQuads(renderWorld, material, pos, randomSupplier, context);
                context.popTransform();

                CopycatRenderContextFabric copycatContext = new CopycatRenderContextFabric(quads, emitter, entry.key());
                entry.part().emitCopycatQuads(entry.key(), state, copycatContext, material);

                context.pushTransform(quad -> {
                    if (occlusionData.isOccluded(quad.cullFace()))
                        return false;
                    // Set cull face to null after culling to avoid interference from sodium
                    quad.cullFace(null);
                    return true;
                });
                meshBuilder.build().outputTo(context.getEmitter());
                context.popTransform();

                // fabric: pop the material changer transform
                if (shouldTransform)
                    context.popTransform();
            } else {
                BakedModel model = getModelForEntry(entry, state, material);
                if (model == null) continue;

                if (entry.part() == null) {
                    model.emitBlockQuads(blockView, state, pos, randomSupplier, context);
                    continue;
                }

                // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
                MeshBuilder meshBuilder = Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).meshBuilder();
                QuadEmitter emitter = meshBuilder.getEmitter();

                List<MutableQuadView> quads = new ArrayList<>();
                context.pushTransform(quad -> {
                    MutableQuadView newQuad = IntermediateMutableQuadView.create();
                    newQuad.copyFrom(quad);
                    quads.add(newQuad);
                    return false;
                });
                model.emitBlockQuads(blockView, state, pos, randomSupplier, context);
                context.popTransform();

                CopycatRenderContextFabric copycatContext = new CopycatRenderContextFabric(quads, emitter, entry.key());
                entry.part().emitCopycatQuads(entry.key(), state, copycatContext, material);

                meshBuilder.build().outputTo(context.getEmitter());
            }
        }
    }

    protected void prepareModelCore(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, BlockState material, Object renderAttachmentData) {
        core.prepareForRender();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(Object data) {
        if (data instanceof BlockState state) {
            BlockState material = getMaterial(state);
            if (!material.is(AllBlocks.COPYCAT_BASE.get()))
                return getIcon(getModelOf(material), null);
        } else if (data instanceof Pair<?, ?> pair && pair.getSecond() instanceof BlockState material) {
            if (!material.is(AllBlocks.COPYCAT_BASE.get()))
                return getIcon(getModelOf(material), pair.getFirst());
        } else if (data instanceof Map<?, ?> mats) {
            for (Map.Entry<?, ?> entry : mats.entrySet()) {
                if (entry.getValue() instanceof Pair<?, ?> pair && pair.getSecond() instanceof BlockState material3) {
                    if (!material3.is(AllBlocks.COPYCAT_BASE.get()))
                        return getIcon(getModelOf(material3), pair.getFirst());
                } else if (entry.getValue() instanceof BlockState material4) {
                    if (!material4.is(AllBlocks.COPYCAT_BASE.get()))
                        return getIcon(getModelOf(material4), null);
                }
            }
        }
        return getIcon(getModelOf(AllBlocks.COPYCAT_BASE.getDefaultState()), null);
    }

    public BakedModel getModelForEntry(CopycatModelCore.ModelEntry entry, BlockState state, BlockState material) {
        if (entry.model() == null)
            return wrapped;
        else {
            if (core.colorize && state.getBlock() instanceof IMultiStateCopycatBlock multiState && AllBlocks.COPYCAT_BASE.has(material)) {
                material = CCBlocks.COPYCAT_BASE.getDefaultState().setValue(BASE_TYPE, multiState.getColorIndex(entry.key()) % BASE_TYPE_COUNT);
            }
            return entry.model().getModel(state, material);
        }
    }

    public static TextureAtlasSprite getIcon(BakedModel model, @Nullable Object data) {
        if (model instanceof CustomParticleIconModel particleIconModel)
            return particleIconModel.getParticleIcon(data);
        return model.getParticleIcon();
    }

    public static BlockState getMaterial(BlockState material) {
        return material == null ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
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
            return face != null && occluded[face.get3DDataValue()];
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
            RenderMaterial renderMaterial = finder.blendMode(blendMode).find();
            return new CopycatModelFabric.MaterialFixer(renderMaterial);
        }
    }
}

