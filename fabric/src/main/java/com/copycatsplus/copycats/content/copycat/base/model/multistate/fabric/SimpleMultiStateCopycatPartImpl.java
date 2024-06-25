package com.copycatsplus.copycats.content.copycat.base.model.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.SimpleMultiStateCopycatPart;
import net.minecraft.client.resources.model.BakedModel;

public class SimpleMultiStateCopycatPartImpl {

    public static BakedModel create(BakedModel original, SimpleMultiStateCopycatPart part) {
        return new SimpleMultiStateCopycatModel(original, part);
    }
}
