package com.ewoudje.waffleblocks.api.compaters;

import com.ewoudje.waffleblocks.api.ClientGridLevel;
import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.impl.GridLevelManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3dc;

import java.util.stream.Stream;

public class Grids {

    public static Stream<? extends Grid> findAt(GridLevel level, Vector3dc p) {
        return findIn(level, new AABB(p.x() - 0.1, p.y() - 0.1, p.z() - 0.1, p.x() + 0.1, p.y() + 0.1, p.z() + 0.1));
    }

    public static Stream<? extends Grid> findAt(GridLevel level, BlockPos p) {
        return findIn(level, new AABB(p));
    }

    public static Stream<? extends Grid> findIn(
            GridLevel level,
            AABB aabb
    ) {
        return level.findGridIn(aabb);
    }

    public static GridLevel getLevel(Level level) {
        return GridLevelManager.getLevel(level);
    }

    public static ClientGridLevel getClientLevel(ClientLevel level) {
        return GridLevelManager.getClientLevel(level);
    }
}
