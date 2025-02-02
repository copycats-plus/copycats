package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Map;

import static net.minecraft.world.level.block.DoorBlock.HALF;

public class CopycatSlidingDoorMovementBehaviour extends SlidingDoorMovementBehaviour {
    @Override
    public void tick(MovementContext context) {
        super.tick(context);

        Map<BlockPos, BlockEntity> tes = context.contraption.presentBlockEntities;
        if (!(tes.get(context.localPos) instanceof CopycatSlidingDoorBlockEntity sdbe))
            return;

        if (sdbe.getPaired() == null) {
            if (sdbe.getBlockState().getValue(HALF) == DoubleBlockHalf.LOWER) {
                sdbe.paired = (CopycatSlidingDoorBlockEntity) tes.get(context.localPos.above());
            } else {
                sdbe.paired = (CopycatSlidingDoorBlockEntity) tes.get(context.localPos.below());
            }
        }
    }
}
