package com.ewoudje.waffleblocks.impl.abst;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.impl.chunk.ChunkGridBackend;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public abstract class AbstractChunkGridBackend implements ChunkGridBackend {
    protected final Grid grid;
    private Consumer<SectionPos> onDirty = a -> {};


    public AbstractChunkGridBackend(Grid grid) {
        this.grid = grid;

    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public void setDirtyListener(Consumer<SectionPos> listener) {
        onDirty = listener;
    }

    @Override
    public void markDirty(SectionPos pos) {
        onDirty.accept(pos);
    }
}
