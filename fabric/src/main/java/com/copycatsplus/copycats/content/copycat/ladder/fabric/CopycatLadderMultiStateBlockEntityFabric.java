package com.copycatsplus.copycats.content.copycat.ladder.fabric;

import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CopycatLadderMultiStateBlockEntityFabric extends CopycatLadderMultiStateBlockEntity implements RenderAttachmentBlockEntity {
    public CopycatLadderMultiStateBlockEntityFabric(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void requestModelUpdate() {

    }

    @Override
    public @Nullable Map<String, BlockState> getRenderAttachmentData() {
        return getMaterialItemStorage().getMaterialMap();
    }
}
