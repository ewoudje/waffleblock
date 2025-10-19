package com.ewoudje.waffleblocks.impl.simple;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.ComponentGetter;
import com.ewoudje.waffleblocks.api.components.ComponentsProvider;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MergedComponentProvider<G extends Grid> implements ComponentsProvider<G> {
    private final Map<GridComponentType<? extends G, ?>, ComponentsProvider<G>> providers = new HashMap<>();
    private final int priority;

    public MergedComponentProvider(int priority) {
        this.priority = priority;
    }


    public void add(ComponentsProvider<G> provider) {
        for (GridComponentType<? extends G, ?> type : provider.getSupportedComponentTypes()) {
            var r = providers.get(type);
            if (r != null && r.getPriority() >= provider.getPriority()) continue;

            providers.put(type, provider);
        }
    }

    @Override
    public @NotNull Set<GridComponentType<? extends G, ?>> getSupportedComponentTypes() {
        return providers.keySet();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public <C> @Nullable ComponentGetter<C> createGetter(GridComponentType<? extends G, C> type) {
        var r = providers.get(type);
        if (r == null) return null;
        return r.createGetter(type);
    }
}
