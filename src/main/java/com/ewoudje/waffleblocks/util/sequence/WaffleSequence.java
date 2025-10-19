package com.ewoudje.waffleblocks.util.sequence;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface WaffleSequence<T> extends Iterable<T> {

    default <R> WaffleSequence<? extends R> sequence(@NotNull WaffleSequenceMapper<? super T, R> mapper) {
        return mapper.buildSequence(this);
    }

    default WaffleSequence<T> filter(@NotNull Predicate<T> p) {
        return new WaffleFilterSequence<>(this, p);
    }

    @NotNull
    default <R> WaffleSequence<R> map(@NotNull Function<T, R> mapper) {
        return new SimpleMappingSequence<>(this, mapper);
    }

    @NotNull
    default <R> WaffleSequence<R> mapNotNull(@NotNull Function<T, R> mapper) {
        return new NotNullMappingSequence<>(this, mapper);
    }

    @NotNull
    default <R> WaffleSequence<Pair<T,R>> pair(@NotNull Function<T, R> mapper) {
        return new SimpleMappingSequence<>(this, v -> Pair.of(v, mapper.apply(v)));
    }

    @NotNull
    default <R> WaffleSequence<Pair<T,R>> pairNotNull(@NotNull Function<T, R> mapper) {
        return new NotNullMappingSequence<>(this, v -> {
            var c = mapper.apply(v);
            return c == null ? null : Pair.of(v, c);
        });
    }


    @Override
    @NotNull
    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

    @NotNull
    Spliterator<T> spliterator();

    @NotNull
    default Stream<T> stream() {
        return stream(false);
    }

    @NotNull
    default Stream<T> stream(boolean parallel) {
        return StreamSupport.stream(spliterator(), parallel);
    }

    @NotNull
    static <T> WaffleSequence<T> of(Stream<T> s) {
        return (Base<T>) s::spliterator;
    }

    @SafeVarargs
    @NotNull
    static <T> WaffleSequence<T> of(T... values) {
        return (Base<T>) () -> Spliterators.spliterator(values, 0);
    }

    @NotNull
    static <T> WaffleSequence<T> add(T pos, @NotNull WaffleSequence<T> seq) {
        return concat(of(pos), seq);
    }

    @NotNull
    static <T> WaffleSequence<T> concat(@NotNull WaffleSequence<T> seq1, @NotNull WaffleSequence<T> seq2) {
        return new ConcatWaffleSequence<>(seq1, seq2);
    }

    static <T> WaffleSequence<T> empty() {
        return of();
    }

    non-sealed interface Base<T> extends WaffleSequence<T> {

    }

    non-sealed interface Derivative<T> extends WaffleSequence<T> {
        Stream<WaffleSequence<?>> getBases();
    }
}
