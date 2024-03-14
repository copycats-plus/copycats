package com.copycatsplus.copycats.mixin.copycat.pressure_plate;

import com.copycatsplus.copycats.content.copycat.base.ICopycatWithWrappedBlock;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Contraption.class)
public class ContraptionMixin {
    @Inject(
            method = "capture",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/Contraption;getBlockEntityNBT(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/nbt/CompoundTag;")
    )
    private void capturePressurePlate(Level world,
                                      BlockPos pos,
                                      CallbackInfoReturnable<Pair<StructureTemplate.StructureBlockInfo, BlockEntity>> cir,
                                      @Local LocalRef<BlockState> stateRef) {
        BlockState state = stateRef.get();
        if (ICopycatWithWrappedBlock.unwrap(state.getBlock()) instanceof PressurePlateBlock) {
            stateRef.set(state.setValue(PressurePlateBlock.POWERED, false));
            world.scheduleTick(pos, state.getBlock(), -1);
        }
    }
}
