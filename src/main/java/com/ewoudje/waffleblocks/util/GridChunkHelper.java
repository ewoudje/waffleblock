package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.WaffleAttachments;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.api.compaters.Grids;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class GridChunkHelper {
    public static Grid getGridOf(LevelChunk chunk) {
        GridChunkAttachment attachment = chunk.getExistingDataOrNull(WaffleAttachments.GRID_CHUNK);
        if (attachment == null) return null;
        return attachment.getGrid(chunk);
    }

    public static Grid getGridOf(Level level, ChunkPos pos) {
        return getGridOf(level.getChunk(pos.x, pos.z));
    }

    public static Grid getGridOf(Level level, int x, int z) {
        return getGridOf(level.getChunk(x, z));
    }

    public static void setGridOf(LevelChunk chunk, Grid grid) {
        chunk.setData(WaffleAttachments.GRID_CHUNK, new GridChunkAttachment(grid));
    }

    public static class GridChunkAttachment {
        public static final Codec<GridChunkAttachment> CODEC = Codec.INT.xmap(GridChunkAttachment::new, a -> a.id);
        private final int id;
        private Grid grid = null;

        public GridChunkAttachment() {
            id = -1;
        }

        public GridChunkAttachment(int gridId) {
            id = gridId;
        }

        public GridChunkAttachment(Grid grid) {
            this.grid = grid;
            this.id = grid.getId();
        }

        public Grid getGrid(LevelChunk chunk) {
            if (grid == null && id != -1) {
                GridLevel gLevel = Grids.getLevel(chunk.getLevel());
                if (gLevel != null) {
                    grid = gLevel.getGrid(id);
                }
            }

            return grid;
        }
    }
}
