package com.copycatsplus.copycats.content.copycat.base.model.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ScaledBlockAndTintGetterForge extends ScaledBlockAndTintGetter {

    public ScaledBlockAndTintGetterForge(String renderingProperty, BlockAndTintGetter wrapped, BlockPos origin, Vec3i originInner, Vec3i scale, Predicate<BlockPos> filter) {
        super(renderingProperty, wrapped, origin, originInner, scale, filter);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @Nullable ModelDataManager getModelDataManager() {
        return wrapped.getModelDataManager();
    }
}
