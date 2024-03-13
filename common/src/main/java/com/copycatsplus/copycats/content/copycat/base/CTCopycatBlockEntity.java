package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface CTCopycatBlockEntity {
    boolean isCTEnabled();

    void setCTEnabled(boolean value);

    void callRedraw();

    boolean copycats$canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState selfState);
}
