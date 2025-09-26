package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class GridComponentType<G extends Grid, COMP> {
    private static final Map<Class<?>, GridComponentType<?, ?>> TYPES = new HashMap<>();

    private final BiConsumer<G, COMP> onAdd, onRemove;
    private final Class<COMP> clazz;


    public GridComponentType(Class<COMP> clazz) {
        this(clazz, (g, c) -> {}, (g, c) -> {});
    }

    public GridComponentType(Class<COMP> clazz, BiConsumer<G, COMP> onAdd, BiConsumer<G, COMP> onRemove) {
        this.onAdd = onAdd;
        this.onRemove = onRemove;
        this.clazz = clazz;

        TYPES.put(clazz, this);
    }

    public Class<COMP> getComponentClass() {
        return clazz;
    }

    public void onAdd(G grid, COMP component) {
        onAdd.accept(grid, component);
    }

    public void onRemove(G grid, COMP component) {
        onRemove.accept(grid, component);
    }

    public boolean isAssignableFrom(GridComponentType<?, ?> type) {
        return clazz.isAssignableFrom(type.clazz);
    }

    public static <C> GridComponentType<?, C> get(Class<C> clazz) {
        return (GridComponentType<?, C>) TYPES.get(clazz);
    }
}
