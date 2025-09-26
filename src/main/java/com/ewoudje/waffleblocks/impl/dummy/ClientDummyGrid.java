package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.ClientGrid;
import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.chunk.ChunkBasedComponent;
import com.ewoudje.waffleblocks.impl.simple.GridComponentTracker;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.util.Map;

public class ClientDummyGrid extends DummyGrid implements ClientGrid {
    private final DummyChunkGridBackend backend;
    private final ChunkBasedComponent comp;

    private final Vector3d nPos = new Vector3d(getPosition());
    private final Quaternionf nRot = new Quaternionf();

    public ClientDummyGrid(int id, Vector3dc pos, Quaternionfc rot, ClientGridLevel level) {
        super(id, pos, rot);
        backend = new DummyChunkGridBackend(this, level.getLevel());
        comp = new ChunkBasedComponent(level, backend);
    }

    @Override
    public <C> C getClientComponent(GridComponentType<ClientGrid, C> componentType) {
        if (componentType.isAssignableFrom(ChunkBasedComponent.TYPE)) {
            return (C) comp;
        }

        return null;
    }

    @Override
    public Vector3dc getPosition(float partialTick) {
        return getPosition().lerp(nPos, partialTick, new Vector3d());
    }

    @Override
    public Quaternionfc getRotation(float partialTick) {
        return nRot;
    }


    @Override
    public <C> @Nullable C getComponent(GridComponentType<? extends Grid, C> componentType) {
        return null;
    }
}
