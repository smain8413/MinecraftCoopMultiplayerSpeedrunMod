package com.github.smain8413.untitled.mixin;

import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageAccessor {
    @Accessor("worldGenerationProgressListener")
    @Mutable
    WorldGenerationProgressListener untitled$setWorldGenerationProgressListener();
}
