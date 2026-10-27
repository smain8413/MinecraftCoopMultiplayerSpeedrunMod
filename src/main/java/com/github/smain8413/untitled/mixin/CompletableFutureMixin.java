package com.github.smain8413.untitled.mixin;

import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

@Mixin(CompletableFuture.class)
public abstract class CompletableFutureMixin<T> implements Future<T>, CompletionStage<T> {
}
