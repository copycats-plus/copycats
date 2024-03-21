package com.copycatsplus.copycats.mixin.copycat.pane;

import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//Glass panes are just iron bars
@Mixin(IronBarsBlock.class)
public abstract class IronBarsMixin {

    @Inject(
            at = @At("HEAD"),
            method = "attachsTo",
            cancellable = true
    )
    private void connectsToCopycat(BlockState pState, boolean pSolidSide, CallbackInfoReturnable<Boolean> cir) {
        if (ICopycatWithWrappedBlock.unwrap(pState.getBlock()) instanceof IronBarsBlock || pState.is(CCTags.commonBlockTag("glass_panes")))
            cir.setReturnValue(true);
    }
}
