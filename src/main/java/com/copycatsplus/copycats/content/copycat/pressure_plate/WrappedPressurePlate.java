package com.copycatsplus.copycats.content.copycat.pressure_plate;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class WrappedPressurePlate {

    public Wood wood(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Wood(sensitivity, properties, type);
    }

    public Stone stone(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Stone(sensitivity, properties, type);
    }

    public Iron iron(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Iron(maxWeight, properties, type);
    }

    public Gold gold(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Gold(maxWeight, properties, type);
    }

    public class Wood extends PressurePlateBlock {

        public Wood(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public class Stone extends PressurePlateBlock {

        public Stone(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public class Iron extends WeightedPressurePlateBlock {

        public Iron(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);
        }

    }

    public class Gold extends WeightedPressurePlateBlock {

        public Gold(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);
        }

    }
}
