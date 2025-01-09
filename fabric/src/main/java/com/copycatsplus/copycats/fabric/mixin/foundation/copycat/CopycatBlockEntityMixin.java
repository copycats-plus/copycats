package com.copycatsplus.copycats.fabric.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatStraightPipeBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.Pair;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Implement platform-specific methods for copycat block entities.
 * <p>
 * All non-multi-state copycats should register their block entities here instead of writing their own platform-specific implementations.
 */
@SuppressWarnings("deprecation")
public class CopycatBlockEntityMixin {
    @Mixin({
            CCCopycatBlockEntity.class,
            CopycatShaftBlockEntity.class,
            CopycatSlidingDoorBlockEntity.class
    })
    public static abstract class BlockEntityWithoutAttachmentData extends SmartBlockEntity implements ICopycatBlockEntity, RenderAttachmentBlockEntity {

        public BlockEntityWithoutAttachmentData(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public @Nullable Object getRenderAttachmentData() {
            return Pair.of(null, getMaterial());
        }
    }

    @Mixin({
            CopycatFluidPipeBlockEntity.class,
    })
    public static abstract class FluidPipeData extends FluidPipeBlockEntity implements ICopycatBlockEntity, RenderAttachmentBlockEntity {

        public FluidPipeData(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public @Nullable Object getRenderAttachmentData() {
            return Pair.of(super.getRenderAttachmentData(), getMaterial());
        }
    }

    @Mixin({
            CopycatStraightPipeBlockEntity.class,
    })
    public static abstract class StraightPipeData extends StraightPipeBlockEntity implements ICopycatBlockEntity, RenderAttachmentBlockEntity {

        public StraightPipeData(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public @Nullable Object getRenderAttachmentData() {
            return Pair.of(super.getRenderAttachmentData(), getMaterial());
        }
    }
}

