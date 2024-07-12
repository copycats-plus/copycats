package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class ScaledBlockAndTintGetterFabric extends ScaledBlockAndTintGetter implements RenderAttachedBlockView {

    private final Object renderData;

    public ScaledBlockAndTintGetterFabric(String renderingProperty, Object renderData, BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter) {
        super(renderingProperty, wrapped, origin, originInner, scale, filter);
        this.renderData = renderData;
    }

    @Override
    public @Nullable Object getBlockEntityRenderAttachment(BlockPos pos) {
        BlockPos truePos = getTruePos(pos);
        if (truePos.equals(origin))
            return renderData;
        else if (wrapped instanceof RenderAttachedBlockView renderView) {
            return renderView.getBlockEntityRenderAttachment(truePos);
        }
        return null;
    }
}
