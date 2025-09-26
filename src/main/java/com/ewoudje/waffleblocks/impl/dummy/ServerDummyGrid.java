package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.ServerGrid;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.simple.GridComponentTracker;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import java.util.Map;

public class ServerDummyGrid extends DummyGrid implements ServerGrid {
    private static final Map<GridComponentType<?, ?>, GridComponentTracker<ServerDummyGrid, ?>> componentTrackers = GridComponentTracker.makeTrackers();

    private final Vector3d nPos = new Vector3d(getPosition());
    private final Quaternionf nRot = new Quaternionf();

    public ServerDummyGrid(int id, Vector3dc pos, Quaternionfc rot) {
        super(id, pos, rot);
    }

    @Override
    public <C> C getServerComponent(GridComponentType<ServerGrid, C> componentType) {
        return null;
    }

    @Override
    public <C> @Nullable C getComponent(GridComponentType<? extends Grid, C> componentType) {
        return null;
    }
}
