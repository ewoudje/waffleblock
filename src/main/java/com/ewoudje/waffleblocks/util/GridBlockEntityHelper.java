package com.ewoudje.waffleblocks.util;

import com.ewoudje.waffleblocks.api.Grid;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GridBlockEntityHelper {

    public static Grid getGridOf(BlockEntity entity) {
        BlockEntityGridContainer container = (BlockEntityGridContainer) entity;
        return container.wb$getGrid();
    }

    public interface BlockEntityGridContainer {
        Grid wb$getGrid();
        void wb$setGrid(Grid grid);
    }

}
