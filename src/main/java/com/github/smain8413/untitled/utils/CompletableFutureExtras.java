package com.github.smain8413.untitled.utils;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

public interface CompletableFutureExtras<T> extends Future<T>, CompletionStage<T> {
    public default boolean isUniCompletion() {
//        this instanceof Unicompletion
        return true;
    }
}
