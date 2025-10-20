package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DelegateGetter<C> implements ComponentGetter<C> {
    private final GridComponentType<?, C> type;
    private ComponentGetter<C> delegate;
    public DelegateGetter(GridComponentType<?, C> type) {
        this.type = type;
    }

    @Override
    public @Nullable C getComponent(@NotNull Grid component) {
        if (delegate == null) {
            if (!GridComponentType.isConfigured())
                throw new IllegalStateException("Trying to access a component when we haven't finished configuring");
            delegate = type.getter();
        }

        return delegate.getComponent(component);
    }
}
