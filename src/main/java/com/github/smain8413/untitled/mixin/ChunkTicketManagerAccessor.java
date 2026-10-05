package com.github.smain8413.untitled.mixin;

import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkTicketManager.class)
public interface ChunkTicketManagerAccessor {
    @Invoker("purge")
    void untitled$purge();

    @Invoker("tick")
    boolean untitled$tick(ThreadedAnvilChunkStorage chunkStorage);
}
