package com.copycatsplus.copycats.content.copycat.base.model.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.model.forge.CopycatModel.OcclusionData;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateTextureAtlasSprite;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class MultiStateCopycatModel extends BakedModelWrapperWithData {

    public static final ModelProperty<Map<String, BlockState>> MATERIALS_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, OcclusionData>> OCCLUSION_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<Map<String, IModelData>> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    public MultiStateCopycatModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                   IModelData blockEntityData) {
        @NotNull Map<String, BlockState> material = getMaterials(blockEntityData);
        if (material.isEmpty())
            return;

        builder.withInitial(MATERIALS_PROPERTY, material);
        builder.with(MATERIALS_PROPERTY, new HashMap<>(material));

        if (!(state.getBlock() instanceof MultiStateCopycatBlock copycatBlock))
            return;

        Map<String, OcclusionData> occlusionMap = material.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> {
            OcclusionData occlusionData = new OcclusionData();
            gatherOcclusionData(world, pos, state, s.getValue(), occlusionData, copycatBlock);
            return occlusionData;
        }));
        builder.withInitial(OCCLUSION_PROPERTY, occlusionMap);


        Map<String, IModelData> wrappedDataMap = material.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> {
            Vec3i inner = copycatBlock.getVectorFromProperty(state, s.getKey());
            ScaledBlockAndTintGetterForge scaledWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, copycatBlock.vectorScale(state), p -> true);
            ScaledBlockAndTintGetterForge filteredWorld = new ScaledBlockAndTintGetterForge(s.getKey(), world, pos, inner, copycatBlock.vectorScale(state),
                    targetPos -> {
                        BlockEntity be = world.getBlockEntity(pos);
                        if (be instanceof MultiStateCopycatBlockEntity mscbe)
                            if (!mscbe.getMaterialItemStorage().getMaterialItem(s.getKey()).enableCT()) return false;
                        return copycatBlock.canConnectTexturesToward(s.getKey(), scaledWorld, pos, targetPos, state);
                    });
            return getModelOf(s.getValue()).getModelData(
                    filteredWorld,
                    pos, s.getValue(), EmptyModelData.INSTANCE);
        }));
        builder.withInitial(WRAPPED_DATA_PROPERTY, wrappedDataMap);
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     OcclusionData occlusionData, MultiStateCopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {

            // Rubidium: Run an additional IForgeBlock.hidesNeighborFace check because it
            // seems to be missing in Block.shouldRenderFace
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

    @NotNull
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {

        // Rubidium: see below
        if (side != null && state.getBlock() instanceof MultiStateCopycatBlock ccb && ccb.shouldFaceAlwaysRender(state, side))
            return Collections.emptyList();

        @NotNull Map<String, BlockState> materials = getMaterials(data);

        if (materials.isEmpty())
            return super.getQuads(state, side, rand, data);

        Map<String, OcclusionData> occlusionData = getOcclusion(data);
        List<BakedQuad> croppedQuads = new LinkedList<>();
        Map<String, IModelData> wrappedData = getWrappedData(data);
        for (Map.Entry<String, BlockState> entry : materials.entrySet()) {
            OcclusionData occlusion = occlusionData.get(entry.getKey());
            if (occlusion == null || occlusion.isOccluded(side))
                continue;
            BlockState material = entry.getValue();
            IModelData dataForMaterial = wrappedData.get(entry.getKey());
            if (dataForMaterial == null)
                dataForMaterial = EmptyModelData.INSTANCE;

            RenderType renderType = MinecraftForgeClient.getRenderType();
            if (renderType != null && !ItemBlockRenderTypes.canRenderInLayer(material, renderType))
                continue;

            emitQuadsForProperty(
                    croppedQuads,
                    getCroppedQuads(entry.getKey(), state, side, rand, material, dataForMaterial, renderType),
                    entry.getKey()
            );

            // Rubidium: render side!=null versions of the base material during side==null,
            // to avoid getting culled away
            if (side == null && state.getBlock() instanceof MultiStateCopycatBlock ccb)
                for (Direction nonOcclusionSide : Iterate.directions)
                    if (ccb.shouldFaceAlwaysRender(state, nonOcclusionSide))
                        emitQuadsForProperty(
                                croppedQuads,
                                getCroppedQuads(entry.getKey(), state, nonOcclusionSide, rand, material, dataForMaterial, renderType),
                                entry.getKey()
                        );
        }

        return croppedQuads;
    }

    protected abstract List<BakedQuad> getCroppedQuads(String key, BlockState state, Direction side, Random rand,
                                                       BlockState material, IModelData wrappedData, RenderType renderType);

    private void emitQuadsForProperty(List<BakedQuad> dest, Collection<BakedQuad> source, String property) {
        for (BakedQuad quad : source) {
            dest.add(new BakedQuad(quad.getVertices(), quad.getTintIndex(), quad.getDirection(), new MultiStateTextureAtlasSprite(property, quad.getSprite()), quad.isShade()));
        }
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        @NotNull Map<String, BlockState> material = getMaterials(data);

        if (material.isEmpty())
            return super.getParticleIcon(data);

        Map.Entry<String, BlockState> key = material.entrySet().stream().findFirst().get();

        return getModelOf(key.getValue()).getParticleIcon(getWrappedData(data).get(key.getKey()));
    }

    public static @NotNull Map<String, BlockState> getMaterials(IModelData data) {
        Map<String, BlockState> materials = data == null ? null : data.getData(MATERIALS_PROPERTY);
        return materials == null ? Map.of() : materials;
    }

    public static @NotNull Map<String, OcclusionData> getOcclusion(IModelData data) {
        Map<String, OcclusionData> occlusions = data == null ? null : data.getData(OCCLUSION_PROPERTY);
        return occlusions == null ? Map.of() : occlusions;
    }

    public static Map<String, IModelData> getWrappedData(IModelData data) {
        Map<String, IModelData> wrappedData = data == null ? null : data.getData(WRAPPED_DATA_PROPERTY);
        return wrappedData == null ? Map.of() : wrappedData;
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }
}
