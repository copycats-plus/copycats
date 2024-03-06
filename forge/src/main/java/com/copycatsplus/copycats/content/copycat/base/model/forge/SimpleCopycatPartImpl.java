package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import net.minecraft.client.resources.model.BakedModel;

public class SimpleCopycatPartImpl implements SimpleCopycatPart {

    public static BakedModel create(BakedModel original, SimpleCopycatPart part) {
        return new SimpleCopycatModel(original, part);
    }
}
