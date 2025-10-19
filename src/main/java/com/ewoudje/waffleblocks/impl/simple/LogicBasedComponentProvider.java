package com.ewoudje.waffleblocks.impl.simple;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.ComponentsProvider;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.api.components.GridLogicType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class LogicBasedComponentProvider<G extends Grid, L>
        implements ComponentsProvider<G> {

    private final GridLogicType<G, L> logicType;
    private final Set<GridComponentType<? extends G, ? super L>> supportedComponents;
    private final Map<G, L> logicStorage = new HashMap<>();
    private final Function<G, L> codecHandler = null; // Placeholder for codec handler

    public LogicBasedComponentProvider(GridLogicType<G, L> logicType) {
        this.logicType = logicType;
        this.supportedComponents = logicType.getSupportedComponents();
    }

    @Override
    public @NotNull Set<GridComponentType<? extends G, ?>> getSupportedComponentTypes() {
        return (Set<GridComponentType<? extends G, ?>>) (Set) supportedComponents;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public @Nullable <C> ComponentGetter<C> createGetter(GridComponentType<? extends G, C> type) {
        if (supportedComponents.contains(type)) {
            final Class<G> gClass = logicType.getGridClass();
            final Function<G, L> constructor = logicType.getConstructor() == null ? codecHandler : logicType.getConstructor();

            return g -> {
                var grid = (G) g;
                var r = logicStorage.get(grid);
                if (r != null) return (C) r;
                if (gClass.isAssignableFrom(grid.getClass()) && logicType.getPredicate().test(grid)) {
                    constructor.apply(grid);
                    logicStorage.put(grid, r);
                    return (C) r;
                } else return null;
            };

        } else {
            return null;
        }
    }
}
