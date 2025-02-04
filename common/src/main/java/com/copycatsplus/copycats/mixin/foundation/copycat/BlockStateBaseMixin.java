package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.compat.Mods;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase")
public class BlockStateBaseMixin {

    @Unique
    private static final TagKey<Block> COPYCAT_BASE = TagKey.create(Registry.BLOCK.key(), new ResourceLocation(Mods.CREATE.id(), "copycat_base"));

    @Inject(
            method = "canOcclude",
            at = @At("HEAD"),
            cancellable = true
    )
    private void customOcclusion(CallbackInfoReturnable<Boolean> cir) {
        BlockState instance = (BlockState) (Object) this;
        try {
            if (instance.is(COPYCAT_BASE)) {
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
