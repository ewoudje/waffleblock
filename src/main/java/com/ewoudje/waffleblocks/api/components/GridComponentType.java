package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.WaffleBlocks;
import com.ewoudje.waffleblocks.WaffleRegistries;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridSide;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class GridComponentType<G extends Grid, COMP> {
    private static final Map<Class<?>, GridComponentType<?, ?>> TYPES = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(GridComponentType.class);
    private static boolean configured = false;
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
        if (configured) throw new IllegalStateException("ComponentTypes have already been configured, but you are creating now later? You should be registering these!");

        this.onAdd = onAdd;
        this.onRemove = onRemove;
        this.clazz = clazz;
        this.gClass = gClass;
        this.side = GridSide.of(gClass);
        TYPES.put(clazz, this);
        getter = new DelegateGetter<>(this);
        predicate = g -> false;
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

    @Nullable
    public static <C> GridComponentType<?, C> get(Class<C> clazz) {
        return (GridComponentType<?, C>) TYPES.get(clazz);
    }

    public static boolean isConfigured() {
        return configured;
    }

    private void configure() {
        assert !configured;


        getter = ((ComponentsProvider<G>) ComponentsProvider.getGlobalComponentsProvider(side)).createGetter(this);
        if (getter == null) {
            LOGGER.warn("GridComponent {} has no implementations!, this means its not used at all?", clazz.getSimpleName());
            getter = g -> null;
        }

        predicate = g -> getter.getComponent(g) != null;
    }

    public static void configureAll() {
        if (configured) return;

        WaffleRegistries.COMPONENTS.forEach(t -> {
            try {
                if (!TYPES.containsKey(t.clazz)) throw new IllegalStateException("Unregistered component?");

                t.configure();
            } catch (Exception e) {
                LOGGER.error("Error while trying to configure GridComponentType {}", t.clazz.getSimpleName(), e);
            }
        });


        configured = true;
    }
}
