package com.copycatsplus.copycats.mixin.copycat.base;

import com.simibubi.create.content.decoration.copycat.FilteredBlockAndTintGetter;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FilteredBlockAndTintGetter.class, remap = false)
public interface FilteredBlockAndTintGetterAccessor {
    @Accessor
    BlockAndTintGetter getWrapped();
}
