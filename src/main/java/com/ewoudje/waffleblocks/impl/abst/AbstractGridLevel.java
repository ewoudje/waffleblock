package com.ewoudje.waffleblocks.impl.abst;

import com.ewoudje.waffleblocks.WaffleRegistries;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.util.WaffleDebug;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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

        //TODO this is bad
        NeoForge.EVENT_BUS.addListener(this::tick);
    }

    @Override
    public @NotNull WaffleSequence<? extends G> getAllGrids() {
        return WaffleSequence.of(gridMap.values().stream());
    }

    @Override
    public @Nullable G getGrid(int gridId) {
        return gridMap.get(gridId);
    }

    private void tick(ServerTickEvent.Post event) {
        Set<G> removed = new HashSet<>();
        gridMap.values().stream()
                .filter(Grid::isRemoved)
                .forEach(removed::add);

        for (G grid : removed) {
            //TODO fire an event
            gridMap.remove(grid.getId());
        }
    }

    protected <C> GridSource<? extends G, C> getGridSource(GridSource.Factory<C> factory) {
        return (GridSource<? extends G, C>) sources.get(WaffleRegistries.GRID_SOURCE.getId(factory));
    }

    protected abstract GridSource<? extends G, ?> createGridSource(GridSource.Factory<?> factory);
}
