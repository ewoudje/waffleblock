package com.ewoudje.waffleblocks.util.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ConcatWaffleSequence<T> implements WaffleSequence.Derivative<T> {
    private final WaffleSequence<T> a, b;

    public ConcatWaffleSequence(WaffleSequence<T> a, WaffleSequence<T> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public @NotNull Spliterator<T> spliterator() {
        return new ConcatSpliterator<>(a.spliterator(), b.spliterator());
    }

    @Override
    public Stream<WaffleSequence<?>> getBases() {
        return Stream.of(a, b);
    }

    private static class ConcatSpliterator<T> implements Spliterator<T> {
        private boolean split = false;
        private boolean lastA = true;
        private final Spliterator<T> a, b;

        private ConcatSpliterator(Spliterator<T> a, Spliterator<T> b) {
            this.a = a;
            this.b = b;
        }


        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (split) {
                return b.tryAdvance(action);
            } else return (lastA = a.tryAdvance(action)) || b.tryAdvance(action);
        }

        @Override
        public Spliterator<T> trySplit() {
            if (!split) {
                split = true;
                if (lastA) {
                    return a;
                }
            }

            return b.trySplit();
        }

        @Override
        public long estimateSize() {
            return a.estimateSize() + b.estimateSize();
        }

        @Override
        public int characteristics() {
            return a.characteristics() & b.characteristics();
        }
    }
}
