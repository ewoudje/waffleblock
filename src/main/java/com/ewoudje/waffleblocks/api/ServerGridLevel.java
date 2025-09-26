package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface ServerGridLevel extends GridLevel{
    @Override
    @NotNull
    ServerLevel getLevel();

    @Override
    @Nullable Stream<? extends ServerGrid> findGridIn(AABB aabb);

    <C> Stream<Pair<ServerGrid, C>> findWithServerComponent(GridComponentType<ServerGrid, C> component);

    <C> ServerGrid createGrid(GridSource.Factory<C> sourceFactory, C context);
}
