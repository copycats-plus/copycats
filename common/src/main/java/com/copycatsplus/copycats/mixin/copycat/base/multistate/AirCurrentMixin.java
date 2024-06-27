package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {

    @Redirect(method = "getFlowLimit", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/decoration/copycat/CopycatBlock;getMaterial(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState copycats$getMultiStateMaterial(BlockGetter cbe, BlockPos reader) {
        return cbe.getBlockState(reader).getBlock() instanceof MultiStateCopycatBlock ? MultiStateCopycatBlock.getMaterial(cbe, reader) : CopycatBlock.getMaterial(cbe, reader);
    }
}
