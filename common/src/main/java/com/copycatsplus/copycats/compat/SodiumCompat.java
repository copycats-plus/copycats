package com.copycatsplus.copycats.compat;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.world.level.BlockGetter;

public class SodiumCompat {
    public static BlockGetter unwrapSodiumLevel(BlockGetter level) {
        if (level instanceof WorldSlice slice) {
            return slice.world;
        }
        return level;
    }
}
