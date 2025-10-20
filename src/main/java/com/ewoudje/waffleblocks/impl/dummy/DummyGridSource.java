package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.GridSource;
import com.ewoudje.waffleblocks.api.components.ComponentsProvider;
import com.ewoudje.waffleblocks.api.components.GridComponentType;
import com.ewoudje.waffleblocks.impl.chunk.ChunkGridBackend;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DummyGridSource implements GridSource<DummyGrid, Void> {

    protected final List<DummyGrid> grids = new ArrayList<>();
    protected final GridLevel level;
    protected ChunkGridBackend backend = null;


    public DummyGridSource(@NotNull GridLevel gLevel) {
        level = gLevel;
        //grids.add(gLevel.createGrid(this, null));
    }

    public ChunkGridBackend getChunkBackend(BlockPos pos) {
        if (pos.getX() < -16 || pos.getZ() < -16) return null;
        if (pos.getX() >= 32 || pos.getZ() >= 32) return null;

        return backend;
    }

    public ChunkGridBackend getChunkBackend(ChunkPos pos) {
        if (pos.x < -1 || pos.x > 1) return null;
        if (pos.z < -1 || pos.z > 1) return null;
        return backend;
    }

    @Override
    public DummyGrid createGrid(int id, Void context) {
        var pos = new Vector3d(64.0, 32.0, 64.0);
        var rot = new Quaternionf();

        return level.getLevel().isClientSide ? new ClientDummyGrid(id, pos, rot, (ClientGridLevel) level) : new ServerDummyGrid(id, pos, rot);
    }

    @Override
    public Factory<Void> getFactory() {
        return null;
    }
}
