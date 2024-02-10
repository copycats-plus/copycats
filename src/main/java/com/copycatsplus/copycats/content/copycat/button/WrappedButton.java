package com.copycatsplus.copycats.content.copycat.button;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class WrappedButton {

    public Wood wood(BlockBehaviour.Properties properties) {
        return new Wood(properties);
    }

    public Stone stone(BlockBehaviour.Properties properties) {
        return new Stone(properties);
    }


    public static class Wood extends ButtonBlock {

        public Wood(BlockBehaviour.Properties pProperties) {
            super(true, pProperties);
        }

        protected @NotNull SoundEvent getSound(boolean pIsOn) {
            return pIsOn ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
        }
    }

    public static class Stone extends ButtonBlock {

        public Stone(BlockBehaviour.Properties pProperties) {
            super(false, pProperties);
        }

        protected @NotNull SoundEvent getSound(boolean pIsOn) {
            return pIsOn ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
        }
    }
}
