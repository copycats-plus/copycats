package com.copycatsplus.copycats.mixin.copycat.button;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.button.WrappedButton;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ButtonBlock.class)
public class ButtonBlockMixin {

    @ModifyArg(
            method = "checkPressed",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"),
            index = 1
    )
    private Block copycats$checkPressed(Block original) {
        if (original instanceof WrappedButton.Wood) {
            return CCBlocks.COPYCAT_WOOD_BUTTON.get();
        }
        if (original instanceof WrappedButton.Stone) {
            return CCBlocks.COPYCAT_STONE_BUTTON.get();
        }
        return original;
    }

    @ModifyArg(
            method = "press",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;I)V"),
            index = 1
    )
    private Block copycats$press(Block original) {
        if (original instanceof WrappedButton.Wood) {
            return CCBlocks.COPYCAT_WOOD_BUTTON.get();
        }
        if (original instanceof WrappedButton.Stone) {
            return CCBlocks.COPYCAT_STONE_BUTTON.get();
        }
        return original;
    }
}
