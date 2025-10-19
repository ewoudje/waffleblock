package com.ewoudje.waffleblocks.api.components;

import com.ewoudje.waffleblocks.api.Grid;
import com.ewoudje.waffleblocks.api.GridLevel;
import com.ewoudje.waffleblocks.util.sequence.NotNullMappingSequence;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequence;
import com.ewoudje.waffleblocks.util.sequence.WaffleSequenceMapper;
import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ComponentGetter<C> extends WaffleSequenceMapper<Grid, C> {

    @Nullable C getComponent(@NotNull Grid component);

    @Override
    default WaffleSequence<C> buildSequence(WaffleSequence<? extends Grid> sequence) {
        return new NotNullMappingSequence<>(sequence, this::getComponent);
    }

    default @NotNull WaffleSequence<? extends C> getAllComponents(@NotNull GridLevel level) {
        return level.getAllGrids().sequence(this);
    }

    default @NotNull WaffleSequence<Pair<? extends Grid, C>> getAllComponentsPaired(GridLevel level) {
        return (WaffleSequence<Pair<? extends Grid, C>>) (Object)
                level.getAllGrids().pairNotNull(this::getComponent);
    }

    default <G extends Grid> @NotNull WaffleSequence<Pair<G, C>> getAllComponentsPaired(GridLevel.I<G> level) {
        return (WaffleSequence<Pair<G, C>>) (Object) getAllComponentsPaired((GridLevel) level);
    }
}
