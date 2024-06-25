package com.copycatsplus.copycats.content.copycat.base.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MultiStateCopycatBlockEntityImpl {
    public static MultiStateCopycatBlockEntity create(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        return new MultiStateCopycatBlockEntityFabric(type, pos, state);
    }
}
