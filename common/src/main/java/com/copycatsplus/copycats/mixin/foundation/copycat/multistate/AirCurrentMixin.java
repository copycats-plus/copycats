package com.copycatsplus.copycats.mixin.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Allow fans to blow through copycat blocks
 */
@Mixin(AirCurrent.class)
public class AirCurrentMixin {

    @Redirect(method = "getFlowLimit", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;getMaterial(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState copycats$getMultiStateMaterial(BlockGetter cbe, BlockPos reader) {
        return ICopycatBlock.getMaterial(cbe, reader);
    }
}
