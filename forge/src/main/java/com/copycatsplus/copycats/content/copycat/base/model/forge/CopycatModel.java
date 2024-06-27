package com.copycatsplus.copycats.content.copycat.base.model.forge;


import com.copycatsplus.copycats.content.copycat.base.CTCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.simibubi.create.AllBlocks;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class CopycatModel extends BakedModelWrapperWithData {

    public static final ModelProperty<BlockState> MATERIAL_PROPERTY = com.simibubi.create.content.decoration.copycat.CopycatModel.MATERIAL_PROPERTY;
    private static final ModelProperty<CopycatModel.OcclusionData> OCCLUSION_PROPERTY = new ModelProperty<>();
    private static final ModelProperty<IModelData> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    public CopycatModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void gatherModelData(ModelDataMap.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, IModelData blockEntityData) {
        BlockState material = getMaterial(blockEntityData);

        builder.withInitial(MATERIAL_PROPERTY, material);

        if (!(state.getBlock() instanceof IFunctionalCopycatBlock copycatBlock))
            return;

        CopycatModel.OcclusionData occlusionData = new CopycatModel.OcclusionData();
        gatherOcclusionData(world, pos, state, material, occlusionData, copycatBlock);
        builder.withInitial(OCCLUSION_PROPERTY, occlusionData);

        IModelData wrappedData = getModelOf(material).getModelData(world, pos, material, EmptyModelData.INSTANCE);
        builder.withInitial(WRAPPED_DATA_PROPERTY, wrappedData);
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material,
                                     CopycatModel.OcclusionData occlusionData, IFunctionalCopycatBlock copycatBlock) {
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
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData data) {
        RenderType renderType = MinecraftForgeClient.getRenderType();
        // Rubidium: see below
        if (side != null && state.getBlock() instanceof IFunctionalCopycatBlock ccb && ccb.shouldFaceAlwaysRender(state, side))
            return Collections.emptyList();

        BlockState material = getMaterial(data);

        CopycatModel.OcclusionData occlusionData = data.getData(OCCLUSION_PROPERTY);
        if (occlusionData != null && occlusionData.isOccluded(side))
            return super.getQuads(state, side, rand, data);

        IModelData wrappedData = data.getData(WRAPPED_DATA_PROPERTY);
        if (wrappedData == null)
            wrappedData = EmptyModelData.INSTANCE;
        if (renderType != null && !ItemBlockRenderTypes.canRenderInLayer(material, renderType))
            return super.getQuads(state, side, rand, data);

        List<BakedQuad> croppedQuads = getCroppedQuads(state, side, rand, material, wrappedData);

        // Rubidium: render side!=null versions of the base material during side==null,
        // to avoid getting culled away
        if (side == null && state.getBlock() instanceof IFunctionalCopycatBlock ccb) {
            boolean immutable = true;
            for (Direction nonOcclusionSide : Iterate.directions)
                if (ccb.shouldFaceAlwaysRender(state, nonOcclusionSide)) {
                    if (immutable) {
                        croppedQuads = new ArrayList<>(croppedQuads);
                        immutable = false;
                    }
                    croppedQuads.addAll(getCroppedQuads(state, nonOcclusionSide, rand, material, wrappedData));
                }
        }

        return croppedQuads;
    }

    /**
     * The returned list must not be mutated.
     */
    public abstract List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand,
                                                    BlockState material, IModelData wrappedData);

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        BlockState material = getMaterial(data);

        IModelData wrappedData = data.getData(WRAPPED_DATA_PROPERTY);
        if (wrappedData == null)
            wrappedData = EmptyModelData.INSTANCE;

        return getModelOf(material).getParticleIcon(wrappedData);
    }

    public static BlockState getMaterial(IModelData data) {
        BlockState material = data == null ? null : data.getData(MATERIAL_PROPERTY);
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
            return face != null && occluded[face.get3DDataValue()];
        }
    }

}
