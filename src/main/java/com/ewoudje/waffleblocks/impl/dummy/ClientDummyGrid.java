package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.chunk.ChunkLogic;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

public class ClientDummyGrid extends DummyGrid implements ClientGrid {
    private final DummyChunkGridBackend backend;
    private final ChunkLogic comp;

    private final Vector3d nPos = new Vector3d(getPosition());
    private final Quaternionf nRot = new Quaternionf();

    public ClientDummyGrid(int id, Vector3dc pos, Quaternionfc rot, ClientGridLevel level) {
        super(id, pos, rot);
        backend = new DummyChunkGridBackend(this, level.getLevel());
        comp = new ChunkLogic(level, backend);
    }

    @Override
    public Vector3dc getPosition(float partialTick) {
        return getPosition().lerp(nPos, partialTick, new Vector3d());
    }

    @Override
    public Quaternionfc getRotation(float partialTick) {
        return nRot;
    }
}
