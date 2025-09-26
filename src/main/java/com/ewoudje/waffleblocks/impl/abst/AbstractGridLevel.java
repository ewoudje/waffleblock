package com.ewoudje.waffleblocks.impl.abst;

import com.ewoudje.waffleblocks.WaffleRegistries;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class AbstractGridLevel<G extends Grid> implements GridLevel {
    protected final ArrayList<GridSource<? extends G, ?>> sources;
    protected final Int2ObjectRBTreeMap<G> gridMap = new Int2ObjectRBTreeMap<>();

    protected AbstractGridLevel() {
        sources = new ArrayList<>();
        for (int i = 0; i < WaffleRegistries.GRID_SOURCE.size(); i++) {
            sources.add(null);
        }

        WaffleRegistries.GRID_SOURCE.forEach(f -> sources.set(WaffleRegistries.GRID_SOURCE.getId(f), createGridSource(f)));
    }

    @Override
    public @Nullable Stream<G> findGridIn(AABB aabb) {
        return sources.stream().flatMap(s -> s.findGridIn(aabb));
    }

    @Override
    public <C> Stream<Pair<? extends Grid, C>> findWithComponent(GridComponentType<Grid, C> component) {
        return sources.stream().flatMap(s -> s.findWithComponent(component));
    }

    @Override
    public @Nullable G getGrid(int gridId) {
        return gridMap.get(gridId);
    }

    protected <C> GridSource<? extends G, C> getGridSource(GridSource.Factory<C> factory) {
        return (GridSource<? extends G, C>) sources.get(WaffleRegistries.GRID_SOURCE.getId(factory));
    }

    protected abstract GridSource<? extends G, ?> createGridSource(GridSource.Factory<?> factory);
}
