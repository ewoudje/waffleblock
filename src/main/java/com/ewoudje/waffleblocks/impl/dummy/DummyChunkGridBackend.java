package com.ewoudje.waffleblocks.impl.dummy;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.impl.abst.AbstractChunkGridBackend;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DummyChunkGridBackend extends AbstractChunkGridBackend {
    private final Level level;
    private Set<ChunkAccess> chunks = null;

    public DummyChunkGridBackend(Grid grid, Level level) {
        super(grid);
        this.level = level;
    }


    @Override
    public Stream<ChunkAccess> getChunks() {
        if (chunks == null) {
            chunks = new HashSet<>();

            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    chunks.add(level.getChunk(x,z, ChunkStatus.FULL));
                }
            }
        }

        return chunks.stream();
    }

    @Override
    public LevelChunkSection getSectionAt(int x, int y, int z) {
        if (x < 0 || z < 0 || x >= 16 || z >= 16) return null;
        var chunk = level.getChunk(0,0);

        return chunk.getSection(chunk.getSectionIndex(y));
    }

    @Override
    public Vec3i getOrigin() {
        return Vec3i.ZERO;
    }

}
