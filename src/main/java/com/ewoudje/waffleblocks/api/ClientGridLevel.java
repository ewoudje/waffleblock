package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface ClientGridLevel extends GridLevel {

    @Override
    @NotNull
    ClientLevel getLevel();

    @Override
    @Nullable Stream<? extends ClientGrid> findGridIn(AABB aabb);

    <C> Stream<Pair<ClientGrid, C>> findWithClientComponent(GridComponentType<ClientGrid, C> component);

    <C> ClientGrid createNewGrid(int gridId, GridSource.Factory<C> factory, C ctx);
}
