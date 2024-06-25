package com.copycatsplus.copycats.content.copycat.ladder.forge;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.forge.MultiStateCopycatModel;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopycatLadderMultiStateBlockEntityForge extends CopycatLadderMultiStateBlockEntity {
    public CopycatLadderMultiStateBlockEntityForge(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void requestModelUpdate() {
        requestModelDataUpdate();
    }

    @Override
    public @NotNull ModelData getModelData() {
         return ModelData.builder()
                .with(MultiStateCopycatModel.MATERIALS_PROPERTY, getMaterialItemStorage().getMaterialMap())
                .build();
    }
}
