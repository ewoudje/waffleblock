package com.ewoudje.waffleblocks.util.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class NotNullMappingSequence<R> implements WaffleSequence.Derivative<R> {
    private final WaffleSequence<?> base;
    private final Function<?, R> mapper;

    public <I> NotNullMappingSequence(WaffleSequence<I> base, @NotNull Function<I, R> mapper) {
        if (base instanceof NotNullMappingSequence<I> mapp) {
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
            final Boolean[] continueAdvancing = {true};
            while (continueAdvancing[0] && base.tryAdvance(v -> {
                R result = ((Function<Object, R>) mapper).apply(v);
                if (result != null) {
                    action.accept(result);
                    continueAdvancing[0] = false;
                }
            }));

            return !continueAdvancing[0];
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
            return base.characteristics() & ~(Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.DISTINCT) | Spliterator.IMMUTABLE | Spliterator.NONNULL;
        }
    }
}
