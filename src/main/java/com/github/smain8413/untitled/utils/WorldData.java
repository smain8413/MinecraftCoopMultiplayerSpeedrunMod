package com.github.smain8413.untitled.utils;

import com.github.smain8413.untitled.ServerWorldExtras;
import com.github.smain8413.untitled.mixin.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.github.smain8413.untitled.Untitled.LOGGER;

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
    public final RegistryKey<World> worldRegistryKey;
    public final ServerChunkManagerAccessor serverChunkManagerAccessor;
    public final ThreadedAnvilChunkStorageAccessor threadedAnvilChunkStorageAccessor;
    public boolean cleared = false;
    public final Path rootDir;
    public final Path dataDir;
    public final File worldDir;

    private static final List<String> NOT_WORLD;

    static {
        NOT_WORLD = new ArrayList<>();
        NOT_WORLD.add("NOT_WORLD");
        NOT_WORLD.add("worldRegistryKey");
        NOT_WORLD.add("cleared");
        NOT_WORLD.add("session");
        NOT_WORLD.add("sessionAccessor");
        NOT_WORLD.add("generatorOptionsAccessor");
        NOT_WORLD.add("Server");
        NOT_WORLD.add("generatorOptionsAccessor");
    }


    public WorldData(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
        this.serverWorldAccessor = (ServerWorldAccessor) serverWorld;
        this.server = serverWorld.getServer();
        this.serverAccessor = (ServerAccessor)server;
        this.session = serverAccessor.untitled$getSession();
        this.sessionAccessor = (SessionAccessor) session;
        this.serverChunkManager = serverWorldAccessor.untitled$getServerChunkManager();
        this.threadedAnvilChunkStorage = serverChunkManager.threadedAnvilChunkStorage;
        this.serverWorldExtra = (ServerWorldExtras) serverWorld;
        this.generatorOptionsAccessor = (GeneratorOptionsAccessor)serverWorld.getServer().getSaveProperties().getGeneratorOptions();
        this.worldRegistryKey = serverWorld.getRegistryKey();
        this.serverChunkManagerAccessor = (ServerChunkManagerAccessor) serverChunkManager;
        threadedAnvilChunkStorageAccessor = (ThreadedAnvilChunkStorageAccessor) threadedAnvilChunkStorage;
        rootDir = session.getDirectory(WorldSavePath.ROOT);
        dataDir = session.getDirectory(WorldSavePath.LEVEL_DAT);
        this.worldDir = session.getWorldDirectory(this.worldRegistryKey);
    }

    public void clearWorldData() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            if (NOT_WORLD.parallelStream().anyMatch(string -> string.equals(field.getName()))) continue;
            try {
                field.setAccessible(true);
                field.set(this, null);
            } catch (IllegalAccessException e) {
                LOGGER.fatal(e.toString());
            }
        }
        cleared = true;
    }

    public boolean isWorld(@Nullable RegistryKey<World> world) {
        if (world == null) {
            LOGGER.fatal(new NullPointerException("Comparison world is null (WorldData.java Line 57)").toString());
            return false;
        }
        return world.equals(this.serverWorld.getRegistryKey());
    }
}