package com.copycatsplus.copycats.content.copycat.base.model.functional.forge;

import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.core.model.Bufferable;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import java.util.Random;

public final class BakedModelWithDataBuilder implements Bufferable {
    private final BakedModel model;
    private BlockAndTintGetter renderWorld = VirtualEmptyBlockGetter.INSTANCE;
    private BlockState referenceState = Blocks.AIR.defaultBlockState();
    private PoseStack poseStack = new PoseStack();
    private BlockPos renderPos = BlockPos.ZERO;
    public static final ModelProperty<Boolean> VIRTUAL_PROPERTY = new ModelProperty<>();
    public static final IModelData VIRTUAL_DATA = new ModelDataMap.Builder().withInitial(VIRTUAL_PROPERTY, true).build();
    private IModelData data = VIRTUAL_DATA;

    public BakedModelWithDataBuilder(BakedModel model) {
        this.model = model;
    }

    public BakedModelWithDataBuilder withRenderPos(BlockPos renderPos) {
        this.renderPos = renderPos;
        return this;
    }

    public BakedModelWithDataBuilder withRenderWorld(BlockAndTintGetter renderWorld) {
        this.renderWorld = renderWorld;
        return this;
    }

    public BakedModelWithDataBuilder withReferenceState(BlockState referenceState) {
        this.referenceState = referenceState;
        return this;
    }

    public BakedModelWithDataBuilder withPoseStack(PoseStack poseStack) {
        this.poseStack = poseStack;
        return this;
    }

    public BakedModelWithDataBuilder withData(IModelData data) {
        this.data = data;
        return this;
    }

    @Override
    public void bufferInto(VertexConsumer consumer, ModelBlockRenderer blockRenderer, Random random) {
        blockRenderer.tesselateBlock(renderWorld, model, referenceState, renderPos, poseStack, consumer, false, random, 42, OverlayTexture.NO_OVERLAY);
    }

    public BlockModel toModel(String name) {
        return BlockModel.of(this, name);
    }

    public BlockModel toModel() {
        return toModel(referenceState.toString());
    }
}
