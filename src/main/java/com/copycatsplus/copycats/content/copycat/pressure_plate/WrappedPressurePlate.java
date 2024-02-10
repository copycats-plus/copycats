package com.copycatsplus.copycats.content.copycat.pressure_plate;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WrappedPressurePlate {

    public Wooden wooden(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties) {
        return new Wooden(sensitivity, properties);
    }

    public Stone stone(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties) {
        return new Stone(sensitivity, properties);
    }

    public HeavyWeighted heavyWeighted(int maxWeight, BlockBehaviour.Properties properties) {
        return new HeavyWeighted(maxWeight, properties);
    }

    public LightWeighted lightWeighted(int maxWeight, BlockBehaviour.Properties properties) {
        return new LightWeighted(maxWeight, properties);
    }

    public static class Wooden extends PressurePlateBlock {

        public Wooden(Sensitivity pSensitivity, Properties pProperties) {
            super(pSensitivity, pProperties);
        }

    }

    public static class Stone extends PressurePlateBlock {

        public Stone(Sensitivity pSensitivity, Properties pProperties) {
            super(pSensitivity, pProperties);
        }

    }

    public static class HeavyWeighted extends WeightedPressurePlateBlock {

        public HeavyWeighted(int pMaxWeight, Properties pProperties) {
            super(pMaxWeight, pProperties);
        }

    }

    public static class LightWeighted extends WeightedPressurePlateBlock {

        public LightWeighted(int pMaxWeight, Properties pProperties) {
            super(pMaxWeight, pProperties);

        }

    }
}
