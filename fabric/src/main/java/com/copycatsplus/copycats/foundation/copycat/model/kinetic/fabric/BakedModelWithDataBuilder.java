package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.mojang.blaze3d.vertex.*;
import dev.engine_room.flywheel.lib.model.baked.EmptyVirtualBlockGetter;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BakedModelWithDataBuilder {
    private final BakedModel model;
    private BlockAndTintGetter renderWorld = EmptyVirtualBlockGetter.FULL_BRIGHT;
    private BlockState referenceState = Blocks.AIR.defaultBlockState();
    private PoseStack poseStack = new PoseStack();
    private BlockPos renderPos = BlockPos.ZERO;

    private static final ThreadLocal<ThreadLocalObjects> THREAD_LOCAL_OBJECTS = ThreadLocal.withInitial(ThreadLocalObjects::new);


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

    public SuperByteBuffer build() {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        ThreadLocalObjects threadLocals = THREAD_LOCAL_OBJECTS.get();

        RandomSource random = threadLocals.random;

        SbbBuilder sbbBuilder = threadLocals.sbbBuilder;
        sbbBuilder.prepare();

        DefaultShadeSeparatedBufferSource bufferSource = threadLocals.defaultBufferSource;
        bufferSource.prepare(sbbBuilder);

        UniversalMeshEmitter universalEmitter = threadLocals.universalEmitter;
        RenderType defaultLayer = ItemBlockRenderTypes.getChunkRenderType(referenceState);
        universalEmitter.prepare(bufferSource, defaultLayer);

        poseStack.pushPose();
        ModelBlockRenderer blockRenderer = dispatcher.getModelRenderer();
        ModelBlockRenderer.enableCaching();
        blockRenderer.tesselateBlock(renderWorld, universalEmitter.wrapModel(model), referenceState, renderPos, poseStack, universalEmitter, false, random, 42, OverlayTexture.NO_OVERLAY);
        ModelBlockRenderer.clearCache();
        poseStack.popPose();

        universalEmitter.clear();
        bufferSource.end();
        return sbbBuilder.build();
    }

    private static class ThreadLocalObjects {
        public final SbbBuilder sbbBuilder = new SbbBuilder();
        public final RandomSource random = RandomSource.createNewThreadLocalInstance();
        public final DefaultShadeSeparatedBufferSource defaultBufferSource = new DefaultShadeSeparatedBufferSource();
        public final UniversalMeshEmitter universalEmitter = new UniversalMeshEmitter();
    }
}
