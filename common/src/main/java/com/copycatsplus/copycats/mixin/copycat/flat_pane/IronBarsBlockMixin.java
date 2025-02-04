package com.copycatsplus.copycats.mixin.copycat.flat_pane;

import com.copycatsplus.copycats.content.copycat.flat_pane.CopycatFlatPaneBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Allow normal panes to attach to copycat flat panes
 */
@Mixin(IronBarsBlock.class)
public class IronBarsBlockMixin {
    @WrapOperation(
            method = "updateShape",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/IronBarsBlock;attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z")
    )
    private boolean attachsToCopycat(IronBarsBlock instance, BlockState state, boolean solidSide, Operation<Boolean> original, @Local(argsOnly = true) Direction direction) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock) {
            Axis axis = state.getValue(CopycatFlatPaneBlock.AXIS);
            return axis != Axis.Y && axis != direction.getAxis();
        }
        return original.call(instance, state, solidSide);
    }

    @WrapOperation(
            method = "getStateForPlacement",
            at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/world/level/block/IronBarsBlock;attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z")
    )
    private boolean attachNorth(IronBarsBlock instance, BlockState state, boolean solidSide, Operation<Boolean> original) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock) {
            Axis axis = state.getValue(CopycatFlatPaneBlock.AXIS);
            return axis != Axis.Y && axis != Direction.NORTH.getAxis();
        }
        return original.call(instance, state, solidSide);
    }

    @WrapOperation(
            method = "getStateForPlacement",
            at = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/world/level/block/IronBarsBlock;attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z")
    )
    private boolean attachSouth(IronBarsBlock instance, BlockState state, boolean solidSide, Operation<Boolean> original) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock) {
            Axis axis = state.getValue(CopycatFlatPaneBlock.AXIS);
            return axis != Axis.Y && axis != Direction.SOUTH.getAxis();
        }
        return original.call(instance, state, solidSide);
    }

    @WrapOperation(
            method = "getStateForPlacement",
            at = @At(ordinal = 2, value = "INVOKE", target = "Lnet/minecraft/world/level/block/IronBarsBlock;attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z")
    )
    private boolean attachWest(IronBarsBlock instance, BlockState state, boolean solidSide, Operation<Boolean> original) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock) {
            Axis axis = state.getValue(CopycatFlatPaneBlock.AXIS);
            return axis != Axis.Y && axis != Direction.WEST.getAxis();
        }
        return original.call(instance, state, solidSide);
    }

    @WrapOperation(
            method = "getStateForPlacement",
            at = @At(ordinal = 3, value = "INVOKE", target = "Lnet/minecraft/world/level/block/IronBarsBlock;attachsTo(Lnet/minecraft/world/level/block/state/BlockState;Z)Z")
    )
    private boolean attachEast(IronBarsBlock instance, BlockState state, boolean solidSide, Operation<Boolean> original) {
        if (state.getBlock() instanceof CopycatFlatPaneBlock) {
            Axis axis = state.getValue(CopycatFlatPaneBlock.AXIS);
            return axis != Axis.Y && axis != Direction.EAST.getAxis();
        }
        return original.call(instance, state, solidSide);
    }
}
