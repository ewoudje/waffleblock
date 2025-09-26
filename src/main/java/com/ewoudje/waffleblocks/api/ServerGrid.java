package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;

public interface ServerGrid extends Grid {

    <C> C getServerComponent(GridComponentType<ServerGrid, C> componentType);
}
