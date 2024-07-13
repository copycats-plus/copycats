package com.copycatsplus.copycats.mixin.copycat.base;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Allow sail-like blocks in copycats to function in windmills
 */
@Mixin(BearingContraption.class)
public class BearingContraptionMixin {
    @Inject(
            method = "getSailBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getSailsInCopycats(Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = capture.getKey().state();
        if (state.getBlock() instanceof IMultiStateCopycatBlock && capture.getRight() instanceof IMultiStateCopycatBlockEntity copycatBE) {
            for (BlockState material : copycatBE.getMaterialItemStorage().getAllMaterials()) {
                if (AllTags.AllBlockTags.WINDMILL_SAILS.matches(material)) {
                    cir.setReturnValue(material);
                    return;
                }
            }
            cir.setReturnValue(state);
            return;
        }
        if (state.getBlock() instanceof ICopycatBlock && capture.getRight() instanceof ICopycatBlockEntity copycatBE) {
            cir.setReturnValue(copycatBE.getMaterial());
            return;
        }
    }
}
