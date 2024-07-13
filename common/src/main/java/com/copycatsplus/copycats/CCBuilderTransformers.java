package com.copycatsplus.copycats;

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
}
