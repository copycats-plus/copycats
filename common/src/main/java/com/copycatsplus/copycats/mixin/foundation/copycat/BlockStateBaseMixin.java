package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public class BlockStateBaseMixin {

    @Unique
    private static final ResourceLocation COPYCAT_BASE = new ResourceLocation(Mods.CREATE.id(), "copycat_base");

    @Inject(
            method = "canOcclude",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customOcclusion(CallbackInfoReturnable<Boolean> cir) {
        BlockBehaviour.BlockStateBase instance = (BlockBehaviour.BlockStateBase) (Object) this;
        try {
            if (instance.getBlockHolder().is(COPYCAT_BASE)) {
                cir.setReturnValue(false);
            }
        } catch (IllegalStateException e) {
            // todo: illegal access if resource location is accessed before registry is initialized
        }
        if (instance.getBlock() instanceof BracketBlock) {
            cir.setReturnValue(false);
        }
    }
}
