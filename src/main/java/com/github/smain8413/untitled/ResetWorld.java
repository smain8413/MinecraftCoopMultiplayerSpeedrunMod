package com.github.smain8413.untitled;

import com.github.smain8413.untitled.mixin.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.SessionLock;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.smain8413.untitled.Untitled.LOGGER;

//@Mixin(MinecraftServer.class)
public abstract class ResetWorld {
    // Add a Queue method in ServerMixin

    public static void Reset(MinecraftServer server, long seed) {
        ServerAccessor serverAccessor = (ServerAccessor) server;
//        server.getWorlds().forEach(world -> world.getPlayers().parallelStream().forEach(world::removePlayer));
        try {
            DeleteWorld(server.getOverworld());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((GeneratorOptionsAccessor)server.getSaveProperties().getGeneratorOptions()).setSeed(seed);
        try {
             DeleteWorld(Objects.requireNonNull(server.getWorld(World.NETHER)));
            DeleteWorld(Objects.requireNonNull(server.getWorld(World.END)));
        } catch (IOException e) {
            LOGGER.fatal(e.toString());
        }
    }

    public static boolean DeleteWorld(ServerWorld world) throws IOException {
        LevelStorage.Session session = ((ServerAccessor) world.getServer()).getSession();
//        SessionAccessor sessionAccessor = (SessionAccessor) session;
        File worldDir = session.getWorldDirectory(world.getRegistryKey());
        WorldReLockData data = UnlockWorld(world);
        boolean success = worldDir.delete();
        ReLockWorld(data, world.getServer(), !world.getRegistryKey().equals(world.getServer().getOverworld().getRegistryKey()));
        return success;
    }

    protected static void EvacuatePlayer(ServerWorld world) {
        List<ServerPlayerEntity> playerEntities = world.getPlayers();
        ServerPlayerEntity[] players = playerEntities.toArray(new ServerPlayerEntity[0]);
//        for (ServerPlayerEntity player : playerEntities) {
        for (int i = 0; i < players.length; i++) {
            ServerPlayerEntity player = players[i];
            ServerWorld endWorld;
            endWorld = world.getServer().getWorld(ServerWorld.END);
            assert endWorld != null;
            if (endWorld.getRegistryKey().equals(world.getRegistryKey())) {
                endWorld = world.getServer().getOverworld(); //TODO confirm this
            }
            BlockPos endWorldSpawnPos = endWorld.getSpawnPos();
            player.teleport(endWorld, endWorldSpawnPos.getX(), endWorldSpawnPos.getY(), endWorldSpawnPos.getZ() + 1, 0, 0);
        }


    }

    protected static List<ServerPlayerEntity> RemoveAndGetPlayers(ServerWorld world) {
        List<ServerPlayerEntity> playerEntities = world.getPlayers();
        ServerPlayerEntity[] players = playerEntities.toArray(new ServerPlayerEntity[0]);
//        if (!playerEntities.isEmpty() && playerEntities != null && world != null)
//            playerEntities.parallelStream().forEach(world::removePlayer);
        // MUST BE fori
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < players.length; i ++) {
                ServerPlayerEntity player = players[i];
                if (player == null) continue;
                world.removePlayer(player);
            }

        return Arrays.asList(players);
    }

    protected static WorldReLockData UnlockWorld(ServerWorld world)  {
//        world.savingDisabled = true;
        ServerWorldAccessor worldAccessor = (ServerWorldAccessor) world;
        ServerChunkManager chunkManager = worldAccessor.untitled$getServerChunkManager();
        LevelStorage.Session session = ((ServerAccessor) world.getServer()).getSession();
        ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
        try {
            //        chunkManagerAccessor.untitled$setThreadedAnvilChunkStorage(threadedAnvilChunkStorage);
            threadedAnvilChunkStorage = UnlockWorld(session, chunkManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        List<ServerPlayerEntity> players = RemoveAndGetPlayers(world);
        EvacuatePlayer(world);
        List<ServerPlayerEntity> players = world.getPlayers();
        return new WorldReLockData(threadedAnvilChunkStorage, players, session, chunkManager);
    }

//    protected record WorldReLockData(ServerChunkManagerAccessor chunkManagerAccessor, List<ServerPlayerEntity> players, LevelStorage.Session session) {}
    protected static class WorldReLockData {
        public ThreadedAnvilChunkStorage threadedAnvilChunkStorage;
        public List<ServerPlayerEntity> players;
        public LevelStorage.Session session;
        public ServerChunkManager serverChunkManager;
        WorldReLockData(ThreadedAnvilChunkStorage threadedAnvilChunkStorage, List<ServerPlayerEntity> players, LevelStorage.Session session, ServerChunkManager serverChunkManager){
            this.threadedAnvilChunkStorage = threadedAnvilChunkStorage;
            this.players = players;
            this.session = session;
            this.serverChunkManager = serverChunkManager;
        }
    }
    private static ThreadedAnvilChunkStorage UnlockWorld(LevelStorage.Session session, ServerChunkManager chunkManager) throws IOException {
//        SessionAccessor sessionAccessor = (SessionAccessor) session;
        ServerChunkManagerAccessor chunkManagerAccessor = (ServerChunkManagerAccessor) chunkManager;
        try {
            session.deleteSessionLock();
        } catch (Exception ignored) {}
        @SuppressWarnings("UnnecessaryLocalVariable") ThreadedAnvilChunkStorage threadedAnvilChunkStorage = chunkManager.threadedAnvilChunkStorage;
//        chunkManagerAccessor.untitled$setThreadedAnvilChunkStorage(null);
        return threadedAnvilChunkStorage;
    }

    protected static void ReLockWorld(WorldReLockData data, MinecraftServer server, boolean movePlayersToOverworld) throws IOException {
//        ThreadedAnvilChunkStorage threadedAnvilChunkStorage = data.threadedAnvilChunkStorage;
//        ServerChunkManagerAccessor serverChunkManagerAccessor = ((ServerChunkManagerAccessor)data.serverChunkManager);
//        serverChunkManagerAccessor.untitled$setThreadedAnvilChunkStorage(threadedAnvilChunkStorage);
        List<ServerPlayerEntity> players = data.players;
        LevelStorage.Session session = data.session;
        SessionAccessor sessionAccessor = (SessionAccessor) session;
        if (movePlayersToOverworld) {
            AtomicReference<ServerWorld> overworld = new AtomicReference<>(server.getOverworld());
            AtomicReference<BlockPos> overWorldSpawn = new AtomicReference<>(overworld.get().getSpawnPos());
            players.parallelStream().forEach(player -> player.teleport(overworld.get(), overWorldSpawn.get().getX(), overWorldSpawn.get().getY(), overWorldSpawn.get().getZ() + 1, 0, 0));
        }
//        server.getWorlds().forEach(world -> world.savingDisabled = false);
        sessionAccessor.SetSessionLock(SessionLock.create(session.getDirectory(WorldSavePath.ROOT)));
    }

    public static void QueueDeleteWorld(ServerWorld world) {/*TODO implement if needed*/}
}
