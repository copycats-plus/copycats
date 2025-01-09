package com.copycatsplus.copycats.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.BlockAndTintGetter;

public class AthenaCompat {
    @ExpectPlatform
    public static BlockAndTintGetter unwrapAthenaGetter(BlockAndTintGetter getter) {
        return getter;
    }
}
