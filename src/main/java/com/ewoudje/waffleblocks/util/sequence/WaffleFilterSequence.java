package com.ewoudje.waffleblocks.util.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WaffleFilterSequence<T> implements WaffleSequence.Derivative<T> {
    private final WaffleSequence<T> base;
    private final Predicate<T> predicate;

    public WaffleFilterSequence(WaffleSequence<T> base, @NotNull Predicate<T> p) {
        if (base instanceof WaffleFilterSequence<T> mapp) {
            this.base = mapp.base;
            this.predicate = mapp.predicate.and(p);
        } else {
            this.base = base;
            this.predicate = p;
        }
    }

    @Override
    public @NotNull Spliterator<T> spliterator() {
        return new FilterSpliterator<>(base.spliterator(), predicate);
    }

    @Override
    public Stream<WaffleSequence<?>> getBases() {
        return Stream.of(base);
    }

    private record FilterSpliterator<T>(Spliterator<T> base, Predicate<T> p) implements Spliterator<T> {
        @Override
        public boolean tryAdvance(java.util.function.Consumer<? super T> action) {
            final Boolean[] continueAdvancing = {true};
            while (continueAdvancing[0] && base.tryAdvance(v -> {
                if (p.test(v)) {
                    action.accept(v);
                    continueAdvancing[0] = false;
                }
            }));

            return !continueAdvancing[0];
        }

        @Override
        public Spliterator<T> trySplit() {
            return new FilterSpliterator<>(base.trySplit(), p);
        }

        @Override
        public long estimateSize() {
            return base.estimateSize();
        }

        @Override
        public int characteristics() {
            return base.characteristics() & ~(Spliterator.SIZED | Spliterator.SUBSIZED);
        }
    }
}
