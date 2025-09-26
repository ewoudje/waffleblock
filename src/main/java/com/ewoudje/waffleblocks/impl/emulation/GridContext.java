package com.ewoudje.waffleblocks.impl.emulation;

import com.ewoudje.waffleblocks.api.Grid;

import java.util.Stack;

public class GridContext {
    private static final ThreadLocal<Stack<Grid>> contexts = ThreadLocal.withInitial(Stack::new);

    public static Grid getCurrentGrid() {
        return contexts.get().peek();
    }

    public static void pushGrid(Grid grid) {
        contexts.get().push(grid);
    }

    public static void popGrid(Grid grid) {
        assert grid == null || grid == contexts.get().pop();
    }

    public static void tickCheck() {
        assert contexts.get().empty();

        if (!contexts.get().empty()) {
            contexts.get().clear();
        }
    }
}
