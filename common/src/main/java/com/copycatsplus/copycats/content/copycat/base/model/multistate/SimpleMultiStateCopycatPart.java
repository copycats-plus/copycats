package com.copycatsplus.copycats.content.copycat.base.model.multistate;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public interface SimpleMultiStateCopycatPart {

    @ExpectPlatform
    static BakedModel create(BakedModel original, SimpleMultiStateCopycatPart part) {
        throw new AssertionError();
    }


    default void emitCopycatQuads(String key, BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        throw new AssertionError("If this is appearing then a model isn't implemented correctly");
    }
}
