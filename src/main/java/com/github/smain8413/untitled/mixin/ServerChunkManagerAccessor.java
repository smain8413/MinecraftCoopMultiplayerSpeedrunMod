package com.github.smain8413.untitled.mixin;

import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerChunkManager.class)
public interface ServerChunkManagerAccessor {
//    @Accessor("threadedAnvilChunkStorage")
//    @Mutable
//    void untitled$setThreadedAnvilChunkStorage(ThreadedAnvilChunkStorage storage);

    @Accessor("ticketManager")
    ChunkTicketManager getTicketManager();
}
