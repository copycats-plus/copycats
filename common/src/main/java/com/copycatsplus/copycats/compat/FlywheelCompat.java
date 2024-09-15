package com.copycatsplus.copycats.compat;

import com.jozufozu.flywheel.core.virtual.VirtualChunk;
import net.minecraft.world.level.BlockGetter;

public class FlywheelCompat {
    public static BlockGetter unwrapFlywheelLevel(BlockGetter level) {
        if (level instanceof VirtualChunk chunk) {
            return chunk.world;
        }
        return level;
    }
}
