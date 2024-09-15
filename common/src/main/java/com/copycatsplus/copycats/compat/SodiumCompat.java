package com.copycatsplus.copycats.compat;

import com.copycatsplus.copycats.mixin.compat.rubidium.WorldSliceAccessor;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.world.level.BlockGetter;

public class SodiumCompat {
    public static BlockGetter unwrapSodiumLevel(BlockGetter level) {
        if (level instanceof WorldSlice slice) {
            return ((WorldSliceAccessor) slice).getWorld();
        }
        return level;
    }
}
