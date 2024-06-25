package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public enum StateType {
    SINGULAR,
    MULTI;

    StateType getTypeFromBlock(Block block) {
        if (block instanceof IStateType stateType) {
            return stateType.stateType();
        } else {
            return SINGULAR;
        }
    }
}
