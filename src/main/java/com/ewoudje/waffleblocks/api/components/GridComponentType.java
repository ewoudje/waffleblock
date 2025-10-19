package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridSide;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class GridComponentType<G extends Grid, COMP> {
    private static final Map<Class<?>, GridComponentType<?, ?>> TYPES = new HashMap<>();
    private final BiConsumer<G, COMP> onAdd, onRemove;
    private Predicate<G> predicate;
    private ComponentGetter<COMP> getter;
    private final GridSide side;
    private final Class<G> gClass;
    private final Class<COMP> clazz;


    public GridComponentType(Class<G> gClass, Class<COMP> clazz) {
        this(gClass, clazz, (g, c) -> {}, (g, c) -> {});
    }

    public GridComponentType(Class<G> gClass, Class<COMP> clazz, BiConsumer<G, COMP> onAdd, BiConsumer<G, COMP> onRemove) {
        this.onAdd = onAdd;
        this.onRemove = onRemove;
        this.clazz = clazz;
        this.gClass = gClass;
        this.side = GridSide.of(gClass);
        TYPES.put(clazz, this);

        configure();
    }

    @NotNull
    public Class<COMP> getComponentClass() {
        return clazz;
    }

    @NotNull
    public Class<G> getGridClass() {
        return gClass;
    }

    public void onAdd(G grid, COMP component) {
        onAdd.accept(grid, component);
    }

    public void onRemove(G grid, COMP component) {
        onRemove.accept(grid, component);
    }

    public boolean isPartOf(GridLogicType<?, ?> type) {
        return clazz.isAssignableFrom(type.getLogicClass());
    }

    @NotNull
    public ComponentGetter<COMP> getter() {
        return getter;
    }

    @NotNull
    public Predicate<G> supports() {
        return predicate;
    }

    @NotNull
    public GridSide side() {
        return side;
    }

    public void configure() {
        getter = ((ComponentsProvider<G>) ComponentsProvider.getGlobalComponentsProvider(side)).createGetter(this);
        if (getter == null) {
            WaffleBlocks.LOGGER.warn("Component {} has no implementations!, this means its not used at all?", clazz.getSimpleName());
            getter = g -> null;
        }

        predicate = g -> getter.getComponent(g) != null;
    }

    @Nullable
    public static <C> GridComponentType<?, C> get(Class<C> clazz) {
        return (GridComponentType<?, C>) TYPES.get(clazz);
    }

    public static Collection<GridComponentType<?, ?>> all() {
        return TYPES.values();
    }
}
