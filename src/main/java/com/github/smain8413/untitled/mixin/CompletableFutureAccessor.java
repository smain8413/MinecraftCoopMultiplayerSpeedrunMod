package com.github.smain8413.untitled.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.CompletableFuture;

@Mixin(targets = "java.util.concurrent.CompletableFuture.UniCompletion")
public interface CompletableFutureAccessor {
//    @Accessor("")
}
