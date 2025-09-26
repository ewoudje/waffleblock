package com.ewoudje.waffleblocks.impl.abst;

import com.ewoudje.waffleblocks.WaffleRegistries;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class AbstractClientGridLevel extends AbstractGridLevel<ClientGrid> implements ClientGridLevel {
    private final ClientLevel level;

    protected AbstractClientGridLevel(ClientLevel level) {
        this.level = level;
    }

    @Override
    public @NotNull ClientLevel getLevel() {
        return level;
    }

    @Override
    public <C> Stream<Pair<ClientGrid, C>> findWithClientComponent(GridComponentType<ClientGrid, C> component) {
        return (Stream<Pair<ClientGrid, C>>) (Object) this.findWithComponent((GridComponentType<Grid, C>) (Object) component); //Most safe cast in history
    }

    @Override
    protected GridSource<? extends ClientGrid, ?> createGridSource(GridSource.Factory<?> factory) {
        return factory.createClientSource(this);
    }

    @Override
    public <C> ClientGrid createNewGrid(int gridId, GridSource.Factory<C> factory, C ctx) {
        GridSource<? extends ClientGrid, C> source = (GridSource<? extends ClientGrid, C>) sources.get(WaffleRegistries.GRID_SOURCE.getId(factory));
        ClientGrid grid =  source.createGrid(gridId, ctx);
        gridMap.put(gridId, grid);

        return grid;
    }
}
