package com.copycatsplus.copycats.mixin.copycat.trapdoor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "trapdoorUsableAsLadder",
            at = @At("HEAD"),
            cancellable = true
    )
    private void copycatUsableAsLadder(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = this.level().getBlockState(pos.below());
        if (blockState.getBlock() instanceof LadderBlock)
            cir.setReturnValue(
                    state.getValue(TrapDoorBlock.OPEN) &&
                            blockState.getValue(LadderBlock.FACING) == state.getValue(TrapDoorBlock.FACING)
            );
    }
}
