package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface GridLevel {

    @NotNull
    Level getLevel();

    /**
     * AABB lookup for any grids within the given coordinates
     * @return all grids that intersect or are contained within the given AABB
     */
    @Nullable
    Stream<? extends Grid> findGridIn(AABB aabb);



    <C> Stream<Pair<? extends Grid, C>> findWithComponent(GridComponentType<Grid, C> component);

    @Nullable
    Grid getGrid(int gridId);
}
