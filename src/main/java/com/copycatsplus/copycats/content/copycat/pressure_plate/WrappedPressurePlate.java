package com.copycatsplus.copycats.content.copycat.pressure_plate;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class WrappedPressurePlate {

    public Wooden wooden(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Wooden(sensitivity, properties, type);
    }

    public Stone stone(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, BlockSetType type) {
        return new Stone(sensitivity, properties, type);
    }

    public HeavyWeighted heavyWeighted(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new HeavyWeighted(maxWeight, properties, type);
    }

    public LightWeighted lightWeighted(int maxWeight, BlockBehaviour.Properties properties, BlockSetType type) {
        return new LightWeighted(maxWeight, properties, type);
    }

    public static class Wooden extends PressurePlateBlock {

        public Wooden(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public static class Stone extends PressurePlateBlock {

        public Stone(Sensitivity pSensitivity, Properties pProperties, BlockSetType pType) {
            super(pSensitivity, pProperties, pType);
        }

    }

    public static class HeavyWeighted extends WeightedPressurePlateBlock {

        public HeavyWeighted(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);
        }

    }

    public static class LightWeighted extends WeightedPressurePlateBlock {

        public LightWeighted(int pMaxWeight, Properties pProperties, BlockSetType pType) {
            super(pMaxWeight, pProperties, pType);

        }

    }
}
