package com.copycatsplus.copycats.mixin.copycat.door;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;

/**
 * Force the schematicannon to place both the upper and lower halves of copycat doors
 */
@Mixin(SchematicannonBlockEntity.class)
public class SchematicannonBlockEntityMixin {
    @Inject(
            method = "shouldIgnoreBlockState",
            at = @At("RETURN"),
            cancellable = true
    )
    private void shouldIgnoreBlockStateMixin(BlockState state, BlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (state.hasProperty(DOUBLE_BLOCK_HALF) && be instanceof ICopycatBlockEntity) {
            cir.setReturnValue(false);
        }
    }

    /**
     * Make an exception for the "protect block entities" setting so that the upper half of a copycat door can be placed
     */
    @WrapOperation(
            method = "shouldPlace",
            at = @At(value = "FIELD", target = "Lcom/simibubi/create/content/schematics/cannon/SchematicannonBlockEntity;replaceBlockEntities:Z"),
            remap = false
    )
    private boolean shouldPlace(SchematicannonBlockEntity instance, Operation<Boolean> original, BlockPos pos, BlockState state, BlockEntity be, BlockState toReplace,
                                BlockState toReplaceOther, boolean isNormalCube) {
        boolean originalReplace = original.call(instance);
        return originalReplace || (state.getBlock() instanceof ICopycatBlock && state.hasProperty(DOUBLE_BLOCK_HALF) && state.getValue(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER);
    }
}
