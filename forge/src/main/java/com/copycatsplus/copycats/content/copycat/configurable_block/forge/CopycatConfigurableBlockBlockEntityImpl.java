package com.copycatsplus.copycats.content.copycat.configurable_block.forge;

import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CopycatConfigurableBlockBlockEntityImpl extends CopycatBlockEntity {
    public CopycatConfigurableBlockBlockEntityImpl(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void cc$requestModelDataUpdate(CopycatBlockEntity instance) {
        instance.requestModelDataUpdate();
    }
}
