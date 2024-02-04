package com.copycatsplus.copycats.content.copycat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class CTWaterloggedCopycatBlock extends ShimWaterloggedCopycatBlock implements ICTCopycatBlock {
    public CTWaterloggedCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult toggleResult = ICTCopycatBlock.super.toggleCT(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (toggleResult.consumesAction()) return toggleResult;
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
