package com.ewoudje.waffleblocks.api;

import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface GridLevel {

    @NotNull Level getLevel();

    @NotNull WaffleSequence<? extends Grid> getAllGrids();

    @Nullable Grid getGrid(int gridId);

    interface I<G extends Grid> extends GridLevel {
        @Override
        @NotNull Level getLevel();

        @Override
        @NotNull WaffleSequence<? extends G> getAllGrids();

        @Override
        @Nullable G getGrid(int gridId);
    }
}
