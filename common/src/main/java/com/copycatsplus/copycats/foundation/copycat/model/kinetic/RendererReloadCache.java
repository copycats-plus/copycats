package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class RendererReloadCache<T, U> {
    public static final Set<RendererReloadCache<?, ?>> ALL = Collections.newSetFromMap(new WeakHashMap<>());
    private final Map<T, U> map = new ConcurrentHashMap<>();

    public RendererReloadCache() {
        synchronized (ALL) {
            ALL.add(this);
        }
    }

    public final U get(T key, Function<T, U> factory) {
        return map.computeIfAbsent(key, factory);
    }

    public final void clear() {
        map.clear();
    }


}

