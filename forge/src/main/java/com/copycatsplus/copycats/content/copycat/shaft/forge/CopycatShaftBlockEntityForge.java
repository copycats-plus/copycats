package com.copycatsplus.copycats.content.copycat.shaft.forge;

import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;


public class CopycatShaftBlockEntityForge extends CopycatShaftBlockEntity {
    public CopycatShaftBlockEntityForge(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return getCopycatBlockEntity().getModelData();
    }
}
