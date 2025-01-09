package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KineticCopycatRendererImpl {
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(new WrappedRenderWorldFabric(be))
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .build();
    }

    @SuppressWarnings("deprecation")
    private static class WrappedRenderWorldFabric extends WrappedRenderWorld implements RenderAttachedBlockView {
        public WrappedRenderWorldFabric(ICopycatBlockEntity be) {
            super(be);
        }

        @Override
        @Nullable
        public Object getBlockEntityRenderAttachment(@NotNull BlockPos pos) {
            return ((RenderAttachedBlockView) level).getBlockEntityRenderAttachment(pos);
        }
    }
}
