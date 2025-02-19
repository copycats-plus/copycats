package com.copycatsplus.copycats.content.copycat.flower_pot;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CopycatFlowerPotBlock extends FlowerPotBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {

    public CopycatFlowerPotBlock(Block content, Properties properties) {
        super(content, properties);
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }
}
