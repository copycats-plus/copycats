package com.copycatsplus.copycats.registrate;

import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Predicate;

public class CopycatRegistrate extends CreateRegistrate {

    private static CopycatRegistrate instance;
    protected CopycatRegistrate(String modid) {
        super(modid);
        instance = this;
    }

    public static CopycatRegistrate create(String modid) {
        return new CopycatRegistrate(modid);
    }

    public <T extends BlockEntity> CopycatBlockEntityBuilder<T, CopycatRegistrate> copycatBlockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return copycatBlockEntity(this, name, factory);
    }

    public <T extends BlockEntity, P> CopycatBlockEntityBuilder<T, P> copycatBlockEntity(P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return (CopycatBlockEntityBuilder)this.entry(name, (callback) -> CopycatBlockEntityBuilder.create(this, parent, name, callback, factory));
    }

    public static class CopycatBlockEntityBuilder<T extends BlockEntity, P> extends CreateBlockEntityBuilder<T, P> {

        public CopycatBlockEntityBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, BlockEntityFactory<T> factory) {
            super(owner, parent, name, callback, factory);
        }

        public static <T extends BlockEntity, P> CopycatBlockEntityBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, BlockEntityFactory<T> factory) {
            return new CopycatBlockEntityBuilder<>(owner, parent, name, callback, factory);
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory) {
            copycatVisual(factory, true);
            return this;
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory, boolean renderNormally) {
            copycatVisual(factory, (be) -> true);
            return this;
        }

        public CopycatBlockEntityBuilder<T, P> copycatVisual(NonNullSupplier<CopycatVisualFactory<T>> factory, Predicate<T> renderNormally) {
            registerVisual(this, factory, renderNormally);
            return this;
        }

        @ExpectPlatform
        public static <T extends BlockEntity, P> void registerVisual(CreateBlockEntityBuilder<T, P> builder, NonNullSupplier<CopycatVisualFactory<T>> factory, Predicate<T> renderNormally) {
            throw new AssertionError();
        }

        @FunctionalInterface
        public interface CopycatVisualFactory<T extends BlockEntity> {
            BlockEntityVisual<? super T> create(VisualizationContext ctx, T be, float partialTicks);
        }
    }

    @ExpectPlatform
    public static <Tab> CreateRegistrate setTab(Tab tab) {
        throw new AssertionError();
    }

    public static CopycatRegistrate getInstance() {
        return instance;
    }
}
