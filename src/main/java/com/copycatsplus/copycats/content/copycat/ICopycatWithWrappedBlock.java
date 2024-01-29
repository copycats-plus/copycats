package com.copycatsplus.copycats.content.copycat;

import net.minecraft.world.level.block.Block;

public interface ICopycatWithWrappedBlock {
    /**
     * Returns any non-copycat block that is representative of this copycat.
     */
    Block getWrappedBlock();

    static Block unwrap(Block block) {
        if (block instanceof ICopycatWithWrappedBlock wrapper) {
            return wrapper.getWrappedBlock();
        }
        return block;
    }
}
