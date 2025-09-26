package com.ewoudje.waffleblocks.impl.chunk;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.ewoudje.waffleblocks.impl.dummy.DummyGridSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface ChunkGridBackend {

    Grid getGrid();
    Stream<ChunkAccess> getChunks();
    LevelChunkSection getSectionAt(int x, int y, int z);
    Vec3i getOrigin();
    void setDirtyListener(Consumer<SectionPos> listener);

    void markDirty(SectionPos pos);
}
