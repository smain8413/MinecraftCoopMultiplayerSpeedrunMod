package com.github.smain8413.untitled.utils;

import com.github.smain8413.untitled.ServerWorldExtras;
import com.github.smain8413.untitled.mixin.GeneratorOptionsAccessor;
import com.github.smain8413.untitled.mixin.ServerAccessor;
import com.github.smain8413.untitled.mixin.ServerWorldAccessor;
import com.github.smain8413.untitled.mixin.SessionAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.level.storage.LevelStorage;

public final class WorldData {
    public final ServerWorld serverWorld;
    public final MinecraftServer server;
    public final ServerAccessor serverAccessor;
    public final ServerWorldAccessor serverWorldAccessor;
    public final LevelStorage.Session session;
    public final SessionAccessor sessionAccessor;
    public final ServerChunkManager serverChunkManager;
    public final ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
    public final ServerWorldExtras serverWorldExtra;
    public final GeneratorOptionsAccessor generatorOptionsAccessor;

    public WorldData(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
        this.serverWorldAccessor = (ServerWorldAccessor) serverWorld;
        this.server = serverWorld.getServer();
        this.serverAccessor = (ServerAccessor)server;
        this.session = serverAccessor.getSession();
        this.sessionAccessor = (SessionAccessor) session;
        this.serverChunkManager = serverWorldAccessor.untitled$getServerChunkManager();
        this.threadedAnvilChunkStorage = serverChunkManager.threadedAnvilChunkStorage;
        this.serverWorldExtra = (ServerWorldExtras) serverWorld;
        this.generatorOptionsAccessor = (GeneratorOptionsAccessor)serverWorld.getServer().getSaveProperties().getGeneratorOptions();
    }
}