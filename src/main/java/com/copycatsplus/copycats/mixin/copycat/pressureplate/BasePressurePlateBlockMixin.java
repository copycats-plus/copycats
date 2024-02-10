package com.copycatsplus.copycats.mixin.copycat.pressureplate;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.pressure_plate.WrappedPressurePlate;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BasePressurePlateBlock.class)
public class BasePressurePlateBlockMixin {
    @ModifyArg(
            method = "checkPressed(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"),
            index = 1
    )
    private Block wrappedBlock(Block original) {
        if (original instanceof WrappedPressurePlate.Wooden) {
            return CCBlocks.COPYCAT_WOODEN_PRESSURE_PLATE.get();
        }
        if (original instanceof WrappedPressurePlate.Stone) {
            return CCBlocks.COPYCAT_STONE_PRESSURE_PLATE.get();
        }
        if (original instanceof WrappedPressurePlate.HeavyWeighted) {
            return CCBlocks.COPYCAT_HEAVY_WEIGHTED_PRESSURE_PLATE.get();
        }
        if (original instanceof WrappedPressurePlate.LightWeighted) {
            return CCBlocks.COPYCAT_LIGHT_WEIGHTED_PRESSURE_PLATE.get();
        }
        return original;
    }
}
