package com.copycatsplus.copycats.foundation.copycat.model.forge;

import com.copycatsplus.copycats.foundation.copycat.model.FilteredBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

import java.util.function.Predicate;

public class FilteredBlockAndTintGetterForge extends FilteredBlockAndTintGetter {
    public FilteredBlockAndTintGetterForge(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        super(wrapped, filter);
    }
}
