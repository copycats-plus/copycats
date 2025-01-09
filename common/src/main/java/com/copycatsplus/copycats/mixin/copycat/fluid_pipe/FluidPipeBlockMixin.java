package com.copycatsplus.copycats.mixin.copycat.fluid_pipe;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Convert a copycat fluid pipe to a copycat glass fluid pipe when wrenched.
 * <p>
 * The reverse conversion doesn't need to be patched because it can be overridden in {@link CopycatGlassFluidPipeBlock}.
 */
@Mixin(value = FluidPipeBlock.class, priority = 1100) // A higher priority is required to apply the mixin after Create: TFMG
public class FluidPipeBlockMixin {
    @WrapOperation(
            method = "onWrenched",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z")
    )
    private boolean onWrenched(Level instance, BlockPos pos, BlockState state, Operation<Boolean> original) {
        if (state.is(AllBlocks.GLASS_FLUID_PIPE.get()) && instance.getBlockState(pos).is(CCBlocks.COPYCAT_FLUID_PIPE.get())) {
            return original.call(instance, pos, CCBlocks.COPYCAT_GLASS_FLUID_PIPE.getDefaultState()
                    .setValue(CopycatGlassFluidPipeBlock.AXIS, state.getValue(GlassFluidPipeBlock.AXIS))
                    .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
        }
        return original.call(instance, pos, state);
    }
}
