package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.AllTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SuppressWarnings("deprecation")
public abstract class WaterloggedCopycatWrappedBlock extends ShimWaterloggedCopycatBlock implements ICopycatWithWrappedBlock {

    public WaterloggedCopycatWrappedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (result == InteractionResult.PASS && !pPlayer.getItemInHand(pHand).is(AllTags.AllItemTags.WRENCH.tag)) {
            return getWrappedBlock().use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        return result;
    }
}
