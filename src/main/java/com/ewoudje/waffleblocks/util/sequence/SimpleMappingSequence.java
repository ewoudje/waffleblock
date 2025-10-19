package com.ewoudje.waffleblocks.util.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimpleMappingSequence<R> implements WaffleSequence.Derivative<R> {
    private final WaffleSequence<?> base;
    private final Function<?, R> mapper;

    public <I> SimpleMappingSequence(WaffleSequence<I> base, @NotNull Function<I, R> mapper) {
        if (base instanceof SimpleMappingSequence<I> mapp) {
            this.base = mapp.base;
            this.mapper = mapp.mapper.andThen(mapper);
        } else {
            this.base = base;
            this.mapper = mapper;
        }
    }

    @Override
    public @NotNull Spliterator<R> spliterator() {
        return new MapSpliterator<>(base.spliterator(), mapper);
    }

    @Override
    public Stream<WaffleSequence<?>> getBases() {
        return Stream.of(base);
    }

    private record MapSpliterator<R>(Spliterator<?> base, Function<?, R> mapper) implements Spliterator<R> {
        @Override
        public boolean tryAdvance(java.util.function.Consumer<? super R> action) {
            return base.tryAdvance(v -> action.accept(((Function<Object, R>) mapper).apply(v)));
        }

        @Override
        public Spliterator<R> trySplit() {
            return new MapSpliterator<>(base.trySplit(), mapper);
        }

        @Override
        public long estimateSize() {
            return base.estimateSize();
        }

        @Override
        public int characteristics() {
            return base.characteristics() & ~(Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.DISTINCT) | Spliterator.IMMUTABLE;
        }
    }
}
