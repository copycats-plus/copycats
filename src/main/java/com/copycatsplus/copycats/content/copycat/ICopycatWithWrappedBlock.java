package com.copycatsplus.copycats.content.copycat;

import net.minecraft.world.level.block.Block;

public interface ICopycatWithWrappedBlock {
    /**
     * Returns any non-copycat block that has the same behavior as the copycat.
     */
    Block getWrappedBlock();

    /**
     * Returns the wrapped block if the provided block is an instance of {@link ICopycatWithWrappedBlock}.
     * Otherwise, return the provided block unmodified.
     */
    static Block unwrap(Block block) {
        if (block instanceof ICopycatWithWrappedBlock wrapper) {
            return wrapper.getWrappedBlock();
        }
        return block;
    }
}
