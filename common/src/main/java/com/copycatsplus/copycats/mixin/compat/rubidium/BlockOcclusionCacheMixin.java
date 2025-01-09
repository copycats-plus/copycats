package com.copycatsplus.copycats.mixin.compat.rubidium;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes sure that copycat blocks are not occluded by Rubidium
 */
@ModMixin(requiredMods = Mods.SODIUM)
@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache")
@Pseudo
public class BlockOcclusionCacheMixin {

    //FIXME: Needs to be done more specifically. Or preferably without stopping their culling on us full stop
    // but right now nothing i've tried seems to be working that makes sense
    // Also need to investigate if this patch is required for Forge, since CopycatModelForge already has a workaround
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true, require = 0)
    private void copycats$stopCullingUs(BlockState selfState, BlockGetter view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (selfState.getBlock() instanceof ICopycatBlock) {
            cir.setReturnValue(true);
        }
    }
}
