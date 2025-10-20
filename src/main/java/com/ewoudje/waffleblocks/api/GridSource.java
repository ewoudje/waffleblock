package com.ewoudje.waffleblocks.api;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface GridSource<G extends Grid, CTX> {

    G createGrid(int id, CTX context);

    Factory<CTX> getFactory();

    interface Factory<CTX> {
        <L extends ClientGridLevel> GridSource<? extends ClientGrid, CTX> createClientSource(L level);
        <L extends ServerGridLevel> GridSource<? extends ServerGrid, CTX> createServerSource(L level);

        StreamCodec<? super RegistryFriendlyByteBuf, CTX> contextCodec();
    }
}
