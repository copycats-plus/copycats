package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains selected block state values detached from the associated block.
 */
@SuppressWarnings("unchecked")
public class PartialModelState {
    private final Map<Property<?>, Object> backingMap = new HashMap<>();

    public int size() {
        return backingMap.size();
    }

    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    public boolean containsKey(Property<?> key) {
        return backingMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    public <T extends Comparable<T>> T get(Property<T> key) {
        return (T) backingMap.get(key);
    }

    @Nullable
    public <T extends Comparable<T>> T put(Property<T> key, T value) {
        return (T) backingMap.put(key, value);
    }

    public <T extends Comparable<T>> T putState(Property<T> key, BlockState value) {
        return (T) backingMap.put(key, value.getValue(key));
    }

    public <T extends Comparable<T>> T remove(Property<T> key) {
        return (T) backingMap.remove(key);
    }

    public void putAll(@NotNull Map<? extends Property<?>, ?> m) {
        backingMap.putAll(m);
    }

    public void clear() {
        backingMap.clear();
    }

    @NotNull
    public Set<Property<?>> keySet() {
        return backingMap.keySet();
    }

    @NotNull
    public Collection<Object> values() {
        return backingMap.values();
    }

    @NotNull
    public Set<Map.Entry<Property<?>, Object>> entrySet() {
        return backingMap.entrySet();
    }

    public static PartialModelState fromBlockState(BlockState state, Property<?>... properties) {
        PartialModelState modelState = new PartialModelState();
        for (Property<?> property : properties) {
            modelState.putState(property, state);
        }
        return modelState;
    }

    public boolean equalsState(BlockState state) {
        for (Map.Entry<Property<?>, Object> entry : backingMap.entrySet()) {
            if (!Objects.equals(state.getValue(entry.getKey()), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PartialModelState that)) return false;
        return Objects.equals(backingMap, that.backingMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(backingMap);
    }

    @Override
    public String toString() {
        return '[' +
                this.backingMap.entrySet().stream()
                        .map(entry -> entry.getKey().getName() + "=" + entry.getValue().toString())
                        .collect(Collectors.joining(",")) +
                ']';
    }
}
