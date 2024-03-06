package com.copycatsplus.copycats.content.copycat.base.model;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;

public interface ToggleableCopycatModel {

    @ExpectPlatform
    public static NonNullFunction<BakedModel, ? extends BakedModel> with(SimpleCopycatPart base, SimpleCopycatPart enhanced) {
        throw new AssertionError();
    }
}
