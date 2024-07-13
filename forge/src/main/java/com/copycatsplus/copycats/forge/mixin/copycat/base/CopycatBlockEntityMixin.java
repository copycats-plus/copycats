package com.copycatsplus.copycats.forge.mixin.copycat.base;

import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.forge.CopycatModelForge;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.forge.KineticCopycatRendererImpl;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.copycatsplus.copycats.utility.BlockEntityUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Implement platform-specific methods for copycat block entities.
 * <p>
 * All non-multi-state copycats should register their block entities here instead of writing their own platform-specific implementations.
 */
@Mixin({
        CCCopycatBlockEntity.class,
        CopycatFluidPipeBlockEntity.class,
        CopycatStraightPipeBlockEntity.class,
        CopycatShaftBlockEntity.class
})
public abstract class CopycatBlockEntityMixin extends SmartBlockEntity implements ICopycatBlockEntity {

    public CopycatBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void redraw() {
        if (!isVirtual())
            requestModelDataUpdate();
        BlockEntityUtils.redraw(this);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return KineticCopycatRendererImpl.mergeData(
                super.getModelData(),
                ModelData.builder()
                        .with(CopycatModelForge.MATERIAL_PROPERTY, getMaterial())
                        .build()
        ).build();
    }
}
