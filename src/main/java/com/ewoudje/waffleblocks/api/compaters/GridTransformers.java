package com.ewoudje.waffleblocks.api.compaters;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import org.joml.Vector3dc;

import java.util.Set;
import java.util.stream.Stream;

public class GridTransformers {
    public static WaffleSequence<Vector3dc> getAllTransformsOf(GridLevel level, Vector3dc pos) {
        return WaffleSequence.add(pos, Grids.findAt(level, pos).map(g -> transform(g, pos)));
    }

    private static Vector3dc transform(Grid g, Vector3dc pos) {
        return null;
    }
}
