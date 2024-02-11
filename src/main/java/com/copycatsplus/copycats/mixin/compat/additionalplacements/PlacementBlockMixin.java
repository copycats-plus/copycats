package com.copycatsplus.copycats.mixin.compat.additionalplacements;

import com.copycatsplus.copycats.content.copycat.pressure_plate.WrappedPressurePlate;
import com.copycatsplus.copycats.content.copycat.stairs.WrappedStairsBlock;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = {
                AdditionalPlacementBlock.class,
                CarpetBlock.class,
                PressurePlateBlock.class,
                SlabBlock.class,
                StairBlock.class,
                WeightedPressurePlateBlock.class
        },
        priority = 1100,
        remap = false
)
public class PlacementBlockMixin {
    @Inject(
            at = @At("HEAD"),
            method = "hasAdditionalStates()Z",
            cancellable = true,
            expect = 0, require = 0
    )
    private void forCopycatBlocks(CallbackInfoReturnable<Boolean> cir) {
        Block block = ((Block) (Object) this);
        if (block instanceof WrappedPressurePlate.Wooden ||
                block instanceof WrappedPressurePlate.Stone ||
                block instanceof WrappedPressurePlate.LightWeighted ||
                block instanceof WrappedPressurePlate.HeavyWeighted ||
                block instanceof WrappedStairsBlock ||
                block instanceof CopycatBlock)
            cir.setReturnValue(false);
    }
}
