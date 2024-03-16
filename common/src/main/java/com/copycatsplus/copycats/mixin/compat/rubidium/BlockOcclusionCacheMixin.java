package com.copycatsplus.copycats.mixin.compat.rubidium;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache")
@Pseudo
public class BlockOcclusionCacheMixin {


    //FIXME: Needs to be done more specifically. Or preferably without stopping their culling on us full stop but right now nothing i've tried seems to be working that makes sense
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private void copycats$stopCullingUs(BlockState selfState, BlockGetter view, BlockPos pos, Direction facing, CallbackInfoReturnable<Boolean> cir) {
        if (selfState.getBlock() instanceof CopycatBlock cb) {
            cir.setReturnValue(true);
        }
    }
}
