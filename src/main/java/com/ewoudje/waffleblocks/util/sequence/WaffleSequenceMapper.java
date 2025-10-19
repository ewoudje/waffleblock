package com.ewoudje.waffleblocks.util.sequence;

public interface WaffleSequenceMapper<I, R> {
    WaffleSequence<? extends R> buildSequence(WaffleSequence<? extends I> sequence);
}
