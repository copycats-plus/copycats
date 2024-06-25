package com.copycatsplus.copycats.content.copycat.base.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public interface SimpleCopycatPart {

    @ExpectPlatform
    static BakedModel create(BakedModel original, SimpleCopycatPart part) {
        throw new AssertionError();
    }


    default void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        throw new AssertionError("If this is appearing then a model isn't implemented correctly");
    }

}
