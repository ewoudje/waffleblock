package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.WaffleComponentProviders;
import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridSide;
import com.ewoudje.waffleblocks.api.ServerGrid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ComponentsProvider<G extends Grid> {
    static ComponentsProvider<Grid> getGlobalSharedComponentsProvider() {
        return WaffleComponentProviders.SHARED_COMPONENTS_PROVIDER;
    }

    static ComponentsProvider<ServerGrid> getGlobalServerComponentsProvider() {
        return WaffleComponentProviders.SERVER_COMPONENTS_PROVIDER;
    }

    static ComponentsProvider<ClientGrid> getGlobalClientComponentsProvider() {
        return WaffleComponentProviders.CLIENT_COMPONENTS_PROVIDER;
    }

    static ComponentsProvider<? extends Grid> getGlobalComponentsProvider(GridSide side) {
        return switch (side) {
            case COMMON -> getGlobalSharedComponentsProvider();
            case SERVER -> getGlobalServerComponentsProvider();
            case CLIENT -> getGlobalClientComponentsProvider();
        };
    }


    @NotNull Set<GridComponentType<? extends G, ?>> getSupportedComponentTypes();

    int getPriority();

    <C> @Nullable ComponentGetter<C> createGetter(GridComponentType<? extends G, C> type);
}
