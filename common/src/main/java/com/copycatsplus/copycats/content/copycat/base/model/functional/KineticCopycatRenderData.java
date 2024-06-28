package com.copycatsplus.copycats.content.copycat.base.model.functional;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.copycatsplus.copycats.utility.ChatUtils;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.config.BackendType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

public record KineticCopycatRenderData(BlockState state, BlockState material, boolean enableCT) {
    public static KineticCopycatRenderData of(IFunctionalCopycatBlockEntity be) {
        if (!CCConfigs.client().disableGraphicsWarnings.get()) {
            if (Backend.getBackendType() != BackendType.INSTANCING &&
                    Minecraft.getInstance().getBlockColors().getColor(be.getMaterial(), null, null, 0) != -1) {
                ChatUtils.sendWarningOnce(
                        "flywheel_block_color",
                        "Block colors may be incorrect due to the current Flywheel rendering backend. Please switch to the instancing backend to fix this."
                );
            }
        }
        return new KineticCopycatRenderData(be.getBlockState(), be.getMaterial(), be.isCTEnabled());
    }
}
