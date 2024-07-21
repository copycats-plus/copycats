package com.copycatsplus.copycats.foundation.copycat.model.forge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.forge.CopycatRenderContextForge;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.forge.ModelDataUtils;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.copycatsplus.copycats.CCBlockStateProperties.BASE_TYPE;
import static com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock.BASE_TYPE_COUNT;
import static com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore.MATERIAL_KEY;
import static com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore.getModelOf;

public class CopycatModelForge extends BakedModelWrapperWithData {

    public static final ModelProperty<BlockState> MATERIAL_PROPERTY = new ModelProperty<>();
    public static final ModelProperty<Map<String, BlockState>> MATERIALS_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, OcclusionData>> OCCLUSION_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, ModelData>> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    private static final ChunkRenderTypeSet allRenderTypes = ChunkRenderTypeSet.of(RenderType.solid(), RenderType.cutout(), RenderType.cutoutMipped(), RenderType.translucent());

    protected final CopycatModelCore core;
    private final boolean disableAO;
    protected final List<CopycatModelCore.ModelEntry> entries = new ArrayList<>();
    private final ThreadLocal<RenderSession> renderSession = ThreadLocal.withInitial(() -> new RenderSession(this::getQuads));

    public CopycatModelForge(BakedModel originalModel, CopycatModelCore core, boolean disableAO) {
        super(originalModel);
        this.core = core;
        this.disableAO = disableAO;
        core.registerModels(entries);
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return !disableAO && super.useAmbientOcclusion();
    }

    @Override
    public boolean useAmbientOcclusion(@NotNull BlockState state) {
        return !disableAO && super.useAmbientOcclusion(state);
    }

    @Override
    public boolean useAmbientOcclusion(@NotNull BlockState state, @NotNull RenderType renderType) {
        return !disableAO && super.useAmbientOcclusion(state, renderType);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        ChunkRenderTypeSet renderTypes = allRenderTypes;
        Map<String, BlockState> materials = getMaterials(data);
        prepareModelCore(state, rand, data);
        for (CopycatModelCore.ModelEntry entry : entries) {
            BlockState material = materials.get(entry.key());
            if (material == null && entry.type().useCopycatLogic())
                continue;
            BakedModel model = getModelForEntry(entry, state, material);
            if (model == null)
                continue;
            if (entry.type().useCopycatLogic()) {
                if (material != null)
                    renderTypes = ChunkRenderTypeSet.union(renderTypes, model.getRenderTypes(material, rand, data));
            } else {
                renderTypes = ChunkRenderTypeSet.union(renderTypes, model.getRenderTypes(state, rand, data));
            }
        }
        return renderTypes;
    }

    @Override
    public ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                             ModelData blockEntityData) {
        if (!(originalModel instanceof BakedModelWrapperWithData)) {
            ModelDataUtils.copyModelData(originalModel.getModelData(world, pos, state, blockEntityData), builder);
        }

        Map<String, BlockState> materials = getMaterials(blockEntityData);
        if (materials.isEmpty()) {
            BlockState material = blockEntityData.get(MATERIAL_PROPERTY);
            if (material != null)
                materials = Map.of(MATERIAL_KEY, material);
        }
        if (materials.isEmpty())
            return builder;

        builder.with(MATERIALS_PROPERTY, new HashMap<>(materials));

        if (!(state.getBlock() instanceof ICopycatBlock copycatBlock))
            return builder;

        if (copycatBlock instanceof IMultiStateCopycatBlock multiStateBlock) {
            Map<String, ModelData> wrappedDataMap = new HashMap<>();
            Map<String, OcclusionData> occlusionMap = new HashMap<>();
            for (Map.Entry<String, BlockState> s : materials.entrySet()) {
                Vec3i inner = multiStateBlock.getVectorFromProperty(state, s.getKey());
                boolean enableCT = !(world.getBlockEntity(pos) instanceof IMultiStateCopycatBlockEntity multiStateBE) || multiStateBE.getMaterialItemStorage().getMaterialItem(s.getKey()).enableCT();
                ScaledBlockAndTintGetter scaledWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, multiStateBlock.vectorScale(state), p -> true);

                OcclusionData occlusionData = new OcclusionData();
                if (!ModelUtil.isVirtual(blockEntityData))
                    gatherOcclusionData(scaledWorld, pos, state, s.getValue(), occlusionData, copycatBlock);
                occlusionMap.put(s.getKey(), occlusionData);

                ScaledBlockAndTintGetter filteredWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, multiStateBlock.vectorScale(state),
                        targetPos -> {
                            if (!enableCT) return false;
                            return multiStateBlock.canConnectTexturesToward(s.getKey(), scaledWorld, pos, targetPos, state);
                        });
                wrappedDataMap.put(s.getKey(), getModelOf(s.getValue()).getModelData(
                        filteredWorld,
                        pos, s.getValue(), ModelData.EMPTY));
            }
            return builder.with(OCCLUSION_PROPERTY, occlusionMap).with(WRAPPED_DATA_PROPERTY, wrappedDataMap);
        } else {
            BlockState material = materials.get(MATERIAL_KEY);
            if (material == null) return builder;

            OcclusionData occlusionData = new OcclusionData();
            if (!ModelUtil.isVirtual(blockEntityData))
                gatherOcclusionData(world, pos, state, material, occlusionData, copycatBlock);
            Map<String, OcclusionData> occlusionMap = Map.of(
                    MATERIAL_KEY,
                    occlusionData
            );
            builder.with(OCCLUSION_PROPERTY, occlusionMap);

            FilteredBlockAndTintGetter filteredWorld = new FilteredBlockAndTintGetterForge(world,
                    targetPos -> {
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof ICopycatBlockEntity copycatBE)
                            if (!copycatBE.isCTEnabled()) return false;
                        return copycatBlock.canConnectTexturesToward(world, pos, targetPos, state);
                    });
            Map<String, ModelData> wrappedDataMap = Map.of(
                    MATERIAL_KEY,
                    getModelOf(material).getModelData(
                            filteredWorld,
                            pos, material, ModelData.EMPTY)
            );
            return builder.with(WRAPPED_DATA_PROPERTY, wrappedDataMap);
        }
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     CopycatModelForge.OcclusionData occlusionData, ICopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {

            // Rubidium: Run an additional IForgeBlock.hidesNeighborFace check because it
            // seems to be missing in Block.shouldRenderFace
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            BlockState neighbourState = world.getBlockState(neighbourPos);
            if (state.supportsExternalFaceHiding() && neighbourState.hidesNeighborFace(world, neighbourPos, state, face.getOpposite())) {
                occlusionData.occlude(face);
                continue;
            }

            if (!Block.shouldRenderFace(state, world, pos, face, neighbourPos))
                occlusionData.occlude(face);
        }
    }

    protected @NotNull List<CopycatRenderContextForge.CopycatBakedQuad> getQuads(BlockState state, @NotNull RandomSource rand, @NotNull ModelData data, RenderType renderType) {

        prepareModelCore(state, rand, data);

        List<CopycatRenderContextForge.CopycatBakedQuad> allQuads = new ArrayList<>();
        Map<String, BlockState> materials = getMaterials(data);
        Map<String, OcclusionData> occlusionDataMap = getOcclusion(data);
        Map<String, ModelData> wrappedDataMap = getWrappedData(data);
        final boolean isVirtual = ModelUtil.isVirtual(data);
        for (CopycatModelCore.ModelEntry entry : entries) {
            BlockState material = materials.get(entry.key());

            if (entry.type().onlyWhenVirtual() && !isVirtual)
                continue;
            if (entry.type().useCopycatLogic() && material == null) {
                if (materials.isEmpty() && isVirtual) {
                    material = AllBlocks.COPYCAT_BASE.getDefaultState();
                } else continue;
            }

            BakedModel model = getModelForEntry(entry, state, material);
            if (model == null) continue;

            BlockState wrappedState = state;
            ModelData wrappedData = data;
            if (entry.type().useCopycatLogic()) {
                wrappedState = material;
                wrappedData = wrappedDataMap.get(entry.key());
                if (wrappedData == null)
                    wrappedData = ModelData.EMPTY;
            }
            if (renderType != null) {
                if (!model.getRenderTypes(wrappedState, rand, wrappedData).contains(renderType))
                    continue;
            }
            if (ModelUtil.isVirtual(wrappedData) != isVirtual) {
                wrappedData = ModelDataUtils.mergeData(wrappedData, ModelUtil.VIRTUAL_DATA).build();
            }

            List<CopycatRenderContextForge.CopycatBakedQuad> quads = new ArrayList<>();
            for (Direction side : Iterate.directions) {
                List<BakedQuad> templateQuads = model.getQuads(wrappedState, side, rand, wrappedData, renderType);
                for (BakedQuad templateQuad : templateQuads) {
                    quads.add(new CopycatRenderContextForge.CopycatBakedQuad(templateQuad, side, entry.key()));
                }
            }
            List<BakedQuad> templateQuads = model.getQuads(wrappedState, null, rand, wrappedData, renderType);
            for (BakedQuad templateQuad : templateQuads) {
                quads.add(new CopycatRenderContextForge.CopycatBakedQuad(templateQuad, null, entry.key()));
            }

            List<CopycatRenderContextForge.CopycatBakedQuad> croppedQuads = getCroppedQuads(entry, state, quads, material);

            CopycatModelForge.OcclusionData occlusionData = occlusionDataMap.get(entry.key());
            for (CopycatRenderContextForge.CopycatBakedQuad croppedQuad : croppedQuads) {
                if (occlusionData != null && occlusionData.isOccluded(croppedQuad.cullFace))
                    continue;

                allQuads.add(croppedQuad);
            }
        }

        return allQuads;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource rand, @NotNull ModelData data, RenderType renderType) {
        List<CopycatRenderContextForge.CopycatBakedQuad> templateQuads = renderSession.get().getQuads(state, rand, data, renderType);
        List<BakedQuad> quads = new ArrayList<>();
        for (CopycatRenderContextForge.CopycatBakedQuad quad : templateQuads) {
            if (side != quad.cullFace)
                continue;
            quads.add(quad.toBakedQuad());
        }
        return quads;
    }

    private List<CopycatRenderContextForge.CopycatBakedQuad> getCroppedQuads(CopycatModelCore.ModelEntry entry, BlockState state, List<CopycatRenderContextForge.CopycatBakedQuad> templateQuads, BlockState material) {
        if (entry.part() == null)
            return templateQuads;
        List<CopycatRenderContextForge.CopycatBakedQuad> quads = new ArrayList<>();
        CopycatRenderContextForge context = new CopycatRenderContextForge(templateQuads, quads, entry.key());
        entry.part().emitCopycatQuads(entry.key(), state, context, material);
        return quads;
    }

    public BakedModel getModelForEntry(CopycatModelCore.ModelEntry entry, BlockState state, BlockState material) {
        if (entry.model() == null)
            return originalModel;
        else {
            if (core.colorize && state.getBlock() instanceof IMultiStateCopycatBlock multiState && AllBlocks.COPYCAT_BASE.has(material)) {
                material = CCBlocks.COPYCAT_BASE.getDefaultState().setValue(BASE_TYPE, multiState.getColorIndex(entry.key()) % BASE_TYPE_COUNT);
            }
            return entry.model().getModel(state, material);
        }
    }

    protected void prepareModelCore(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        core.prepareForRender();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        @NotNull Map<String, BlockState> material = getMaterials(data);

        if (material.isEmpty())
            return super.getParticleIcon(data);

        Map.Entry<String, BlockState> key = material.entrySet().stream().findFirst().get();

        return getModelOf(key.getValue()).getParticleIcon(getWrappedData(data).get(key.getKey()));
    }

    public static @NotNull BlockState getMaterial(ModelData data) {
        BlockState material = data == null ? null : data.get(MATERIAL_PROPERTY);
        return material == null ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    public static @NotNull Map<String, BlockState> getMaterials(ModelData data) {
        Map<String, BlockState> materials = data == null ? null : data.get(MATERIALS_PROPERTY);
        return materials == null ? Map.of() : materials;
    }

    public static @NotNull Map<String, OcclusionData> getOcclusion(ModelData data) {
        Map<String, OcclusionData> occlusions = data == null ? null : data.get(OCCLUSION_PROPERTY);
        return occlusions == null ? Map.of() : occlusions;
    }

    public static @NotNull Map<String, ModelData> getWrappedData(ModelData data) {
        Map<String, ModelData> wrappedData = data == null ? null : data.get(WRAPPED_DATA_PROPERTY);
        return wrappedData == null ? Map.of() : wrappedData;
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

    @FunctionalInterface
    public interface Renderer {
        List<CopycatRenderContextForge.CopycatBakedQuad> getQuads(BlockState state, @NotNull RandomSource rand, @NotNull ModelData data, RenderType renderType);
    }

    public static class RenderSession implements Renderer {
        private final Renderer renderer;
        private BlockState state = null;
        private RandomSource rand = null;
        private ModelData data = null;
        private RenderType renderType = null;
        private List<CopycatRenderContextForge.CopycatBakedQuad> result = null;

        public RenderSession(Renderer renderer) {
            this.renderer = renderer;
        }

        @Override
        public List<CopycatRenderContextForge.CopycatBakedQuad> getQuads(BlockState state, @NotNull RandomSource rand, @NotNull ModelData data, RenderType renderType) {
            if (Objects.equals(this.state, state) && this.rand == rand && this.data == data && this.renderType == renderType && this.result != null) {
                return result;
            }
            this.state = state;
            this.rand = rand;
            this.data = data;
            this.renderType = renderType;
            this.result = renderer.getQuads(state, rand, data, renderType);
            return result;
        }
    }
}
