package com.copycatsplus.copycats.mixin.copycat.sliding_door;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlidingDoorBlockEntity.class)
public interface SlidingDoorBlockEntityAccessor {
    @Accessor
    LerpedFloat getAnimation();
}
