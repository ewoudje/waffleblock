package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridSide;
import com.ewoudje.waffleblocks.api.ServerGrid;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GridLogicType<G extends Grid, L> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GridLogicType.class);
    private final Class<L> clazz;
    private final Class<G> gClass;
    private final Set<GridComponentType<? extends G, ? super L>> supportedComponents;
    private final Predicate<G> predicate;
    private final Function<G, L> constructor;
    private final Codec<Function<G, L>> codec;
    private final GridSide side;
    private final Supplier<ComponentsProvider<G>> selfProviding;

    private GridLogicType(
            @NotNull Class<L> clazz,
            @NotNull Class<G> gClass,
            @NotNull Predicate<G> predicate,
            @Nullable Codec<Function<G, L>> codec,
            @Nullable Function<G, L> constructor,
            @Nullable Supplier<ComponentsProvider<G>> selfProviding
    ) {
        this.constructor = constructor;
        this.codec = codec;
        this.clazz = clazz;
        this.predicate = predicate;
        this.side = GridSide.of(gClass);
        this.gClass = gClass;
        this.selfProviding = selfProviding;

        //noinspection unchecked
        supportedComponents = (Set<GridComponentType<? extends G, ? super L>>) (Set) Arrays.stream(clazz.getInterfaces())
                .map(GridComponentType::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    public Set<GridComponentType<? extends G, ? super L>> getSupportedComponents() {
        return supportedComponents;
    }

    public Class<L> getLogicClass() {
        return clazz;
    }

    public Class<G> getGridClass() {
        return gClass;
    }

    public GridSide getSide() {
        return side;
    }

    public Function<G, L> getConstructor() {
        return constructor;
    }

    public Codec<Function<G, L>> getCodec() {
        return codec;
    }

    public Supplier<ComponentsProvider<G>> selfProviding() {
        return selfProviding;
    }

    public Predicate<G> getPredicate() {
        return predicate;
    }


    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2, Predicate<G> predicate, Codec<Function<G, L>> codec) {
        return new GridLogicType<>(clazz, clazz2, predicate, codec, null, null);
    }

    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2, Predicate<G> predicate, Function<G, L> constructor) {
        return new GridLogicType<>(clazz, clazz2, predicate, null, constructor, null);
    }

    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2, Predicate<G> predicate, Supplier<ComponentsProvider<G>> selfProviding) {
        return new GridLogicType<>(clazz, clazz2, predicate, null, null, selfProviding);
    }

    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2, Codec<Function<G, L>> codec) {
        return new GridLogicType<>(clazz, clazz2, g -> true, codec, null, null);
    }

    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2, Function<G, L> constructor) {
        return new GridLogicType<>(clazz, clazz2, g -> true, null, constructor, null);
    }
    public static <G extends Grid, L> GridLogicType<G, L> createLogic(Class<L> clazz, Class<G> clazz2,  Supplier<ComponentsProvider<G>> selfProviding) {
        return new GridLogicType<>(clazz, clazz2, g -> true, null, null, selfProviding);
    }
}
