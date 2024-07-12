package com.copycatsplus.copycats.mixin.compat.additionalplacements;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.firemerald.additionalplacements.block.AdditionalPlacementBlock;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Always report that our blocks have no extra states added by AdditionalPlacements.
 */
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
            require = 0
    )
    private void forCopycatBlocks(CallbackInfoReturnable<Boolean> cir) {
        Block block = ((Block) (Object) this);
        if (block instanceof ICopycatBlock)
            cir.setReturnValue(false);
    }
}
