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

    public Heavy heavy(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Heavy(maxWeight, properties, type);
    }

    public Light light(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Light(maxWeight, properties, type);
    }

    public static class Wood extends PressurePlateBlock {

        public Wood(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public static class Stone extends PressurePlateBlock {

        public Stone(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public static class Heavy extends WeightedPressurePlateBlock {

        public Heavy(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);
        }

    }

    public static class Light extends WeightedPressurePlateBlock {

        public Light(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);

        }

    }
}
