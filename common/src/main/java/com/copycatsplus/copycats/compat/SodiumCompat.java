package com.copycatsplus.copycats.compat;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.BlockGetter;

import java.lang.reflect.Field;

public class SodiumCompat {

    public static BlockGetter unwrapSodiumLevel(BlockGetter level) {
        if (level instanceof WorldSlice slice) {
            return getPrivateField(slice);
        }
        return level;
    }

    private static ClientLevel getPrivateField(WorldSlice instance) {
        try {
            Field f = WorldSlice.class.getDeclaredField("world");
            f.setAccessible(true);
            return (ClientLevel) f.get(instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access field", e);
        }
    }
}