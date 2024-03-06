package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.world.level.block.Block;

public interface ICopycatWithWrappedBlock<B extends Block> {
    /**
     * Returns any non-copycat block that has the same behavior as the copycat.
     */
     B getWrappedBlock();

    /**
     * Returns the wrapped block if the provided block is an instance of {@link ICopycatWithWrappedBlock}.
     * Otherwise, return the provided block unmodified.
     */
    static <B extends Block> Block unwrap(B block) {
        if (block instanceof ICopycatWithWrappedBlock<?> wrapper) {
            return wrapper.getWrappedBlock();
        }
        return block;
    }
}
