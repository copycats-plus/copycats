package com.copycatsplus.copycats.registrate.forge;

import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import com.copycatsplus.copycats.registrate.CopycatRegistrate.CopycatBlockEntityBuilder.CopycatVisualFactory;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public abstract class CopycatRegistrateCopycatBlockEntityBuilderImpl extends CopycatRegistrate {

    protected CopycatRegistrateCopycatBlockEntityBuilderImpl(String modid) {
        super(modid);
    }

    public static <T extends BlockEntity, P> void registerVisual(CreateBlockEntityBuilder<T, P> builder, NonNullSupplier<CopycatVisualFactory<T>> factory, Predicate<T> renderNormally) {
        builder.visual(() -> ((visualizationContext, t, v) -> factory.get().create(visualizationContext, t, v)), renderNormally::test);
    }
}
