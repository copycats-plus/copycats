package com.copycatsplus.copycats.compat.fabric;

import net.minecraft.world.level.BlockAndTintGetter;

public class AthenaCompatImpl {
    public static BlockAndTintGetter unwrapAthenaGetter(BlockAndTintGetter getter) {
        // no-op since Athena isn't available below 1.19.4
        return getter;
    }
}
