package com.ewoudje.waffleblocks.api.compaters;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import org.joml.Vector3dc;

import java.util.Set;
import java.util.stream.Stream;

public class GridTransformers {
    public static Stream<Vector3dc> getAllTransformsOf(GridLevel level, Vector3dc pos) {
        return Stream.concat(Stream.of(pos), Grids.findAt(level, pos).map(g -> transform(g, pos)));
    }

    private static Vector3dc transform(Grid g, Vector3dc pos) {
        return null;
    }
}
