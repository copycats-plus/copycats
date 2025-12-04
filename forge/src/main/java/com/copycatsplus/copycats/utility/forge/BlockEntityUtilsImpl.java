package com.copycatsplus.copycats.utility.forge;

import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlockEntity;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelVisual;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftVisual;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityUtilsImpl {
    public static void requestModelDataUpdate(BlockEntity blockEntity) {
        blockEntity.requestModelDataUpdate();
    }

    public static CreateBlockEntityBuilder<CopycatShaftBlockEntity, CreateRegistrate> addShaftVisual(CreateBlockEntityBuilder<CopycatShaftBlockEntity, CreateRegistrate> shaft) {
        return shaft.visual(() -> CopycatShaftVisual::new, false);
    }

    public static CreateBlockEntityBuilder<CopycatCogWheelBlockEntity, CreateRegistrate> addCogWheelVisual(CreateBlockEntityBuilder<CopycatCogWheelBlockEntity, CreateRegistrate> cogwheel) {
        return cogwheel.visual(() -> CopycatCogWheelVisual::new, false);
    }
}
