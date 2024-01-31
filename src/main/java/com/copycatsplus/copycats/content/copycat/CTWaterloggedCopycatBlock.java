package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class CTWaterloggedCopycatBlock extends WaterloggedCopycatBlock {
    public CTWaterloggedCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        CopycatBlockEntity be = getBlockEntity(level, pos);
        if (be instanceof CTCopycatBlockEntity ctbe) {
            if (!ctbe.isCTEnabled()) return state;
        }
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.isCrouching() && pPlayer.getItemInHand(pHand).equals(ItemStack.EMPTY)) {
            CopycatBlockEntity be = getBlockEntity(pLevel, pPos);
            if (be instanceof CTCopycatBlockEntity ctbe) {
                ctbe.setCTEnabled(!ctbe.isCTEnabled());
                ctbe.callRedraw();
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
