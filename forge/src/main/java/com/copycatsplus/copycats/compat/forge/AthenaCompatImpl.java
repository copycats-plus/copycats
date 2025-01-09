package com.copycatsplus.copycats.compat.forge;

import earth.terrarium.athena.api.client.forge.WrappedGetter;
import net.minecraft.world.level.BlockAndTintGetter;

public class AthenaCompatImpl {
    public static BlockAndTintGetter unwrapAthenaGetter(BlockAndTintGetter getter) {
        if (getter instanceof WrappedGetter wrapped) {
            return wrapped.getter();
        }
        return getter;
    }
}
