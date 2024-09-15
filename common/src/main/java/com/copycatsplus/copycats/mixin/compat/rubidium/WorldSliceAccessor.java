package com.copycatsplus.copycats.mixin.compat.rubidium;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldSlice.class)
public interface WorldSliceAccessor {
    @Accessor
    Level getWorld();
}
