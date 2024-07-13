package com.copycatsplus.copycats.utility.forge;

import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityUtilsImpl {
    public static void requestModelDataUpdate(BlockEntity blockEntity) {
        blockEntity.requestModelDataUpdate();
    }
}
