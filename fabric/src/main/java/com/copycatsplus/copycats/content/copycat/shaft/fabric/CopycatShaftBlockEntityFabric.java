package com.copycatsplus.copycats.content.copycat.shaft.fabric;

import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CopycatShaftBlockEntityFabric extends CopycatShaftBlockEntity implements RenderAttachmentBlockEntity {
    public CopycatShaftBlockEntityFabric(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public BlockState getRenderAttachmentData() {
        return getMaterial();
    }
}
