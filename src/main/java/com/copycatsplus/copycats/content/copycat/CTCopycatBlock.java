package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class CTCopycatBlock extends CopycatBlock implements ICTCopycatBlock {
    public CTCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        if (!allowCTAppearance(state, level, pos, side, queryState, queryPos))
            return state;
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult toggleResult = ICTCopycatBlock.super.toggleCT(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (toggleResult.consumesAction()) return toggleResult;
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
