package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import java.util.stream.Stream;

public interface GridSource<G extends Grid, C> {

    @Nullable
    Stream<G> findGridIn(AABB aabb);


    default <C> Stream<Pair<Grid, C>> findWithComponent(GridComponentType<Grid, C> component) {
        return (Stream<Pair<Grid, C>>) (Object) findWithMyComponent((GridComponentType<G, C>) component);
    }

    <C> Stream<Pair<G, C>> findWithMyComponent(GridComponentType<G, C> component);

    G createGrid(int id, C context);

    Factory<C> getFactory();

    interface Factory<C> {
        <L extends ClientGridLevel> GridSource<? extends ClientGrid, C> createClientSource(L level);
        <L extends ServerGridLevel> GridSource<? extends ServerGrid, C> createServerSource(L level);

        StreamCodec<? super RegistryFriendlyByteBuf, C> contextCodec();
    }
}
