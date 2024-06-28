package com.copycatsplus.copycats.content.copycat.base.model.functional.fabric;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.model.functional.WrappedRenderWorld;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunctionalCopycatRenderHelperImpl {
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, IFunctionalCopycatBlockEntity be, PoseStack ms) {
        return new BakedModelWithDataBuilder(model)
                .withRenderWorld(new WrappedRenderWorldFabric(be).setCTMode(true))
                .withRenderPos(be.getBlockPos())
                .withReferenceState(be.getBlockState())
                .withPoseStack(ms)
                .build();
    }

    @SuppressWarnings("deprecation")
    private static class WrappedRenderWorldFabric extends WrappedRenderWorld implements RenderAttachedBlockView {
        public WrappedRenderWorldFabric(IFunctionalCopycatBlockEntity be) {
            super(be);
        }

        @Override
        @Nullable
        public Object getBlockEntityRenderAttachment(@NotNull BlockPos pos) {
            return ((RenderAttachedBlockView) level).getBlockEntityRenderAttachment(pos);
        }
    }
}
