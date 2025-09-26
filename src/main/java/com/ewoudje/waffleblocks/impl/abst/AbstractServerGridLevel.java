package com.ewoudje.waffleblocks.impl.abst;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.ServerGrid;
import com.ewoudje.waffleblocks.api.ServerGridLevel;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.payloads.NewGridPacket;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class AbstractServerGridLevel extends AbstractGridLevel<ServerGrid> implements ServerGridLevel {
    private final ServerLevel level;
    private int counter = 0;

    protected AbstractServerGridLevel(ServerLevel level) {
        this.level = level;
    }

    @Override
    public <C> ServerGrid createGrid(GridSource.Factory<C> sourceFactory, C context) {
        PacketDistributor.sendToAllPlayers(new NewGridPacket<>(counter, sourceFactory, context)); // TODO send to all players viewing this grid
        ServerGrid grid = getGridSource(sourceFactory).createGrid(counter++, context);
        gridMap.put(counter - 1, grid);

        return grid;
    }

    @Override
    public @NotNull ServerLevel getLevel() {
        return level;
    }

    @Override
    public <C> Stream<Pair<ServerGrid, C>> findWithServerComponent(GridComponentType<ServerGrid, C> component) {
        return (Stream<Pair<ServerGrid, C>>) (Object) this.findWithComponent((GridComponentType<Grid, C>) (Object) component); //Most safe cast in history
    }

    @Override
    protected GridSource<? extends ServerGrid, ?> createGridSource(GridSource.Factory factory) {
        return factory.createServerSource(this);
    }
}
