package com.github.smain8413.untitled;

import com.github.smain8413.untitled.utils.QueuedAction;
import com.github.smain8413.untitled.utils.WorldData;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import net.minecraft.data.SnbtProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.level.storage.SessionLock;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.github.smain8413.untitled.Untitled.LOGGER;

public class WorldReset {
    public static void Reset(MinecraftServer server) {
        deleteWorld(server.getWorld(ServerWorld.OVERWORLD));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected static void deleteWorld(ServerWorld world) {
        WorldData data = new WorldData(world);
//        if (true) return;
//        data.server.save(false, true, false);
        // unlock
//        Path WorldDir = data.session.getDirectory(WorldSavePath.ROOT);
//        Path DataDir = data.session.getDirectory(WorldSavePath.LEVEL_DAT);
        WorldData safeWorld = evacuatePlayers(data);
//        try {
//            unlock(data);
//        } catch (IOException e) {
//            LOGGER.fatal(e.toString());
//        }
//        data.generatorOptionsAccessor.setSeed(new Random().nextLong());
        deleteWorldDirectory(data);
//        try {
//            lock(data);
//        } catch (IOException e) {
//            LOGGER.fatal(e.toString());
//        }
//        data.clearWorldData();
        findAndEditLevelDat(data);
//        data.serverAccessor.getWorlds().replace(World.OVERWORLD, null);
        // change seed


        // queue re-lock
        queueRunAction(safeWorld, new QueuedAction(() -> {
//            findAndEditLevelDat(safeWorld);
            safeWorld.serverAccessor.untitled$createWorlds(new WorldGenerationProgressListener() {
                @Override
                public void start(ChunkPos spawnPos) {
                }

                @Override
                public void setChunkStatus(ChunkPos pos, @Nullable ChunkStatus status) {
                }

                @Override
                public void stop() {
                }
            });
//            findAndEditLevelDat(data);
            //            @SuppressWarnings("OptionalGetWithoutIsPresent") File levelData = Arrays.stream(Objects.requireNonNull(safeWorld.session.getDirectory(WorldSavePath.ROOT).toFile().listFiles())).filter(file -> file.getName().equals("level.dat")).findFirst().get();
//            safeWorld.generatorOptionsAccessor.setSeed(new Random().nextLong());
//            safeWorld.generatorOptionsAccessor
//            evacuatePlayers(safeWorld);
//            safeWorld.serverAccessor.untitled$createWorlds(new WorldGenerationProgressListener() {
//                @Override
//                public void start(ChunkPos spawnPos) {
//                }
//
//                @Override
//                public void setChunkStatus(ChunkPos pos, @Nullable ChunkStatus status) {
//                }
//
//                @Override
//                public void stop() {
//                }
//            });
            killEveryone(safeWorld);
//            data.dataDir.toFile().setWritable(true);
//            data.dataDir.getParent().resolve("level.dat_old").toFile().setWritable(true);
//            safeWorld.clearWorldData();
//            data.generatorOptionsAccessor.setSeed(new Random().nextLong());
        }, 3));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected static void findAndEditLevelDat(WorldData data) {
        if (!data.dataDir.toFile().canWrite()) data.dataDir.toFile().setWritable(true);
//        Path directoryPath = data.session.getDirectory(WorldSavePath.ROOT);
//        File directory = directoryPath.toFile();
        //noinspection ConstantValue
        do { // level.dat
            Path levelDatOld = data.dataDir.getParent().resolve("level.dat_old");
            if (!levelDatOld.toFile().canWrite()) levelDatOld.toFile().setWritable(true);
            try {
                NbtIo.writeCompressed(data.newLevelDat, Files.newOutputStream(data.dataDir));
                if (levelDatOld.toFile().exists())
                    NbtIo.writeCompressed(data.newLevelDat, Files.newOutputStream(levelDatOld));
            } catch (IOException e) {
                LOGGER.fatal(e.fillInStackTrace());
            }

            try {
                GZIPInputStream gzip = null;
                gzip = new GZIPInputStream(Files.newInputStream(data.dataDir));
                NbtIo.read(new DataInputStream(gzip));
                if (levelDatOld.toFile().exists()) {
                    NbtIo.readCompressed(Files.newInputStream(levelDatOld));
                    gzip = new GZIPInputStream(Files.newInputStream(levelDatOld));
                    NbtIo.read(new DataInputStream(gzip));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("dataDir setReadonly: %s%n", data.dataDir.toFile().setReadOnly());
            if (levelDatOld.toFile().exists()) levelDatOld.toFile().setReadOnly();

        } while (false);
//        System.out.println(directory.toAbsolutePath());

    }

    protected static void unlock(WorldData data) throws IOException {
        // region experimental
        try {
            //            data.threadedAnvilChunkStorageAccessor.untitled$save(true);
//            data.serverChunkManager.tick(() -> true);
//            for (int i = 0; i < 4; i++) {
//                data.serverChunkManagerAccessor.getChunkCache()[i] = null;
//            }
//            data.serverChunkManager.close();
//            data.serverChunkManager.
            ;
        } catch (Exception e) {
            LOGGER.fatal(e.toString());
        }
        // endregion experimental
        data.session.deleteSessionLock();
    }

    protected static void lock(WorldData data) throws IOException {
        data.sessionAccessor.SetSessionLock(SessionLock.create(data.session.getDirectory(WorldSavePath.ROOT)));
    }

    protected static WorldData evacuatePlayers(WorldData data) {
        ServerWorld destination = Objects.requireNonNull(data.server.getWorld(data.isWorld(World.OVERWORLD) ? World.END : World.OVERWORLD));
        for (int i = 0; i < data.serverWorld.getPlayers().size(); i++) {
            ServerPlayerEntity player = data.serverWorld.getPlayers().get(i);
            BlockPos destinationSpawn = destination.getSpawnPos();
            if (destination.getRegistryKey().equals(ServerWorld.END)) destinationSpawn.add(0, 100000, 0);
            player.teleport(destination, destinationSpawn.getX(), destinationSpawn.getY() + 1, destinationSpawn.getZ(), 0, 0);
        }
        return new WorldData(destination);
    }

    // send players back to overworld through death
    protected static void killEveryone(WorldData data) {
        List<ServerPlayerEntity> players = data.serverWorld.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).kill();
        }
    }

    protected static void queueRunAction(WorldData data, QueuedAction action) {
        data.serverWorldExtra.untitled$runNextTick(action);
    }

    protected static void deleteWorldDirectory(WorldData data) {
//        File worldDirectory = data.session.getWorldDirectory(data.worldRegistryKey);
//        File[] files = worldDirectory.listFiles();
//        for (int i = 0; i < (files != null ? files.length : 0); i++) {
//            System.out.println("d1:"+files[i]+"d2:"+ Arrays.toString(files[i].listFiles()));
//
//        }
        try {
            recursiveCleanDirectory(data.worldDir, List.of("level.dat", "raids.dat", "raids_end.dat", "icon.png", "stats", "datapacks", "session.lock"));
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    public static void recursiveCleanDirectory(File dir, @Nullable List<String> ignoredFileNames) throws IOException {
//        dir.setReadable(true);
        if (!dir.exists()) return;
        do {
            if (ignoredFileNames == null) {
                break;
            }
            if (ignoredFileNames.parallelStream().filter(Objects::nonNull).anyMatch(name -> name.equals(dir.getName()))) {
                return;
            }
        } while (false);
        if (!dir.isDirectory()) {
            FileUtils.forceDelete(dir);
//            dir.delete();
            return;
        }
        if (dir.listFiles() == null) return;

        for (File file: Objects.requireNonNull(dir.listFiles())) {
            recursiveCleanDirectory(file, ignoredFileNames);
        }
    }
}
