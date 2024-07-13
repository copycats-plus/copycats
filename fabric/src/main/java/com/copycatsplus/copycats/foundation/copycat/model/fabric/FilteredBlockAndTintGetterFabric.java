package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class FilteredBlockAndTintGetterFabric extends FilteredBlockAndTintGetter implements RenderAttachedBlockView {
    private final Object renderData;
    private final BlockAndTintGetter wrapped;
    private final BlockPos origin;

    public FilteredBlockAndTintGetterFabric(Object renderData, BlockAndTintGetter wrapped, BlockPos origin, Predicate<BlockPos> filter) {
        super(wrapped, filter);
        this.renderData = renderData;
        this.wrapped = wrapped;
        this.origin = origin;
    }

    @Deprecated
    @Nullable
    public Object getBlockEntityRenderAttachment(BlockPos pos) {
        if (pos.equals(origin))
            return renderData;
        else if (wrapped instanceof RenderAttachedBlockView renderView) {
            return renderView.getBlockEntityRenderAttachment(pos);
        }
        return null;
    }
}
