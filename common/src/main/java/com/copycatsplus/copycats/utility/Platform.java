package com.copycatsplus.copycats.utility;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public enum Platform {
    FORGE, FABRIC;

    public static final Platform CURRENT = getCurrent();

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    @ApiStatus.Internal
    @ExpectPlatform
    public static Platform getCurrent() {
        throw new AssertionError();
    }

    public enum Environment {
        CLIENT, SERVER;


        public static final Environment CURRENT = getCurrent();

        public boolean isCurrent() {
            return this == CURRENT;
        }

        public void runIfCurrent(Supplier<Runnable> run) {
            if (isCurrent())
                run.get().run();
        }

        public <T> T getIfCurrent(Supplier<T> supplier) {
            return isCurrent() ? supplier.get() : null;
        }

        public <T> T getIfCurrent(Supplier<T> supplier, T defaultValue) {
            return isCurrent() ? supplier.get() : defaultValue;
        }

        @ApiStatus.Internal
        @ExpectPlatform
        public static Environment getCurrent() {
            throw new AssertionError();
        }
    }
}
