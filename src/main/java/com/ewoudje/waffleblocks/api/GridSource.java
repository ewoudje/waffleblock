package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.stream.Stream;

public interface GridSource<G extends Grid, CTX> {
    WaffleSequence<G> getAllGrids();

    G createGrid(int id, CTX context);

    Factory<CTX> getFactory();

    interface Factory<CTX> {
        <L extends ClientGridLevel> GridSource<? extends ClientGrid, CTX> createClientSource(L level);
        <L extends ServerGridLevel> GridSource<? extends ServerGrid, CTX> createServerSource(L level);

        StreamCodec<? super RegistryFriendlyByteBuf, CTX> contextCodec();
    }
}
