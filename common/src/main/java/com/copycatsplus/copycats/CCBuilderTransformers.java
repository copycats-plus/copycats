package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.casing.WrappedCasingBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.world.level.block.Block;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CCBuilderTransformers {

    @ExpectPlatform
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        throw new AssertionError("Shouldn't appear");
    }

    @ExpectPlatform
    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        throw new AssertionError("Shouldn't appear");
    }

    @ExpectPlatform
    public static <B extends WrappedCasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedCasing(CTSpriteShiftEntry spriteShift) {
        throw new AssertionError("Shouldn't appear");
    }

    @ExpectPlatform
    public static <B extends CasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedLayeredCasing(
            CTSpriteShiftEntry ct, CTSpriteShiftEntry ct2) {
        throw new AssertionError("Shouldn't appear");
    }
}
