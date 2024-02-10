package com.copycatsplus.copycats.content.copycat.button;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class WrappedButton {

    public Wood wood(BlockBehaviour.Properties properties, BlockSetType type, int ticksToStayPressed, boolean arrowsCanPress) {
        return new Wood(properties, type, ticksToStayPressed, arrowsCanPress);
    }

    public Stone stone(BlockBehaviour.Properties properties, BlockSetType type, int ticksToStayPressed, boolean arrowsCanPress) {
        return new Stone(properties, type, ticksToStayPressed, arrowsCanPress);
    }


    public static class Wood extends ButtonBlock {

        public Wood(Properties pProperties, BlockSetType pType, int pTicksToStayPressed, boolean pArrowsCanPress) {
            super(pProperties, pType, pTicksToStayPressed, pArrowsCanPress);
        }
    }

    public static class Stone extends ButtonBlock {

        public Stone(Properties pProperties, BlockSetType pType, int pTicksToStayPressed, boolean pArrowsCanPress) {
            super(pProperties, pType, pTicksToStayPressed, pArrowsCanPress);
        }
    }
}
