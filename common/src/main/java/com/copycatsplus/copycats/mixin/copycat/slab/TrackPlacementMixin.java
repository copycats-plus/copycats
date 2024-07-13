package com.copycatsplus.copycats.mixin.copycat.slab;

import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.trains.track.TrackPlacement;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Allow copycat slabs to be placed when paving tracks.
 */
@Mixin(TrackPlacement.class)
public class TrackPlacementMixin {
    @WrapOperation(
            method = "paveTracks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;")
    )
    private static Block getInnerBlock(BlockItem instance, Operation<Block> original) {
        if (instance.getBlock() instanceof CopycatSlabBlock)
            return Blocks.SMOOTH_STONE_SLAB;
        return original.call(instance);
    }

    @Inject(
            at = @At(value = "NEW", target = "()Ljava/util/HashSet;"),
            method = "paveTracks"
    )
    private static void revertBlock(Level level,
                                    TrackPlacement.PlacementInfo info,
                                    BlockItem blockItem,
                                    boolean simulate,
                                    CallbackInfo ci,
                                    @Local LocalRef<Block> block) {
        block.set(blockItem.getBlock());
    }
}
