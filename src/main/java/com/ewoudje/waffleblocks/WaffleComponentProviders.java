package com.ewoudje.waffleblocks;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.ServerGrid;
import com.ewoudje.waffleblocks.impl.simple.MergedComponentProvider;

public class WaffleComponentProviders {
    public static final MergedComponentProvider<ClientGrid> CLIENT_COMPONENTS_PROVIDER = new MergedComponentProvider<>(0);
    public static final MergedComponentProvider<ServerGrid> SERVER_COMPONENTS_PROVIDER = new MergedComponentProvider<>(0);
    public static final MergedComponentProvider<Grid>       SHARED_COMPONENTS_PROVIDER = new MergedComponentProvider<>(0);
}
