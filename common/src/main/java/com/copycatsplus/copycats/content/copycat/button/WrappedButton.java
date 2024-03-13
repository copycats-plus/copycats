package com.copycatsplus.copycats.content.copycat.button;

import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WrappedButton {

    public Wood wood(BlockBehaviour.Properties properties) {
        return new Wood(properties);
    }

    public Stone stone(BlockBehaviour.Properties properties) {
        return new Stone(properties);
    }


    public static class Wood extends WoodButtonBlock {

        public Wood(Properties pProperties) {
            super(pProperties);
        }
    }

    public static class Stone extends StoneButtonBlock {

        public Stone(Properties pProperties) {
            super(pProperties);
        }
    }
}
