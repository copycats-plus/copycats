package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import net.minecraft.world.level.block.Block;

/**
 * Represents the type of a copycat.
 *
 * @deprecated To identify copycats, check that the block class is an instanceof {@link ICopycatBlock}. To identify multi-state copycats, check that the block class is an instanceof {@link IMultiStateCopycatBlock}.
 */
@Deprecated(since = "1.4")
public enum StateType {
    SINGULAR,
    MULTI;

    public static StateType getTypeFromBlock(Block block) {
        if (block instanceof IStateType stateType) {
            return stateType.stateType();
        } else {
            return SINGULAR;
        }
    }
}
