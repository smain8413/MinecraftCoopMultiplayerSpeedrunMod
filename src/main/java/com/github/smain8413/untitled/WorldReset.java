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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import static com.github.smain8413.untitled.Untitled.LOGGER;

public class WorldReset {
    public static void Reset(MinecraftServer server) {
        deleteWorld(server.getWorld(ServerWorld.OVERWORLD));
    }

    protected static void deleteWorld(ServerWorld world) {
        WorldData data = new WorldData(world);
        data.server.save(false, true, false);
        // unlock
        Path WorldDir = data.session.getDirectory(WorldSavePath.ROOT);
        Path DataDir = data.session.getDirectory(WorldSavePath.LEVEL_DAT);
        WorldData safeWorld = evacuatePlayers(data);
//        try {
//            unlock(data);
//        } catch (IOException e) {
//            LOGGER.fatal(e.toString());
//        }
//        data.generatorOptionsAccessor.setSeed(new Random().nextLong());
        deleteWorldDirectory(data);
        try {
            lock(data);
        } catch (IOException e) {
            LOGGER.fatal(e.toString());
        }
//        data.clearWorldData();
        findAndEditLevelDat(data);
//        data.serverAccessor.getWorlds().replace(World.OVERWORLD, null);
        // change seed


        // queue re-lock
        queueRunAction(safeWorld, new QueuedAction(() -> {
//            @SuppressWarnings("OptionalGetWithoutIsPresent") File levelData = Arrays.stream(Objects.requireNonNull(safeWorld.session.getDirectory(WorldSavePath.ROOT).toFile().listFiles())).filter(file -> file.getName().equals("level.dat")).findFirst().get();
            safeWorld.generatorOptionsAccessor.setSeed(new Random().nextLong());
//            safeWorld.generatorOptionsAccessor
//            evacuatePlayers(safeWorld);
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
            killEveryone(safeWorld);
//            safeWorld.clearWorldData();
//            data.generatorOptionsAccessor.setSeed(new Random().nextLong());
        }, 100));
    }

    protected static void findAndEditLevelDat(WorldData data) {
//        Path directoryPath = data.session.getDirectory(WorldSavePath.ROOT);
//        File directory = directoryPath.toFile();
        do { // level.dat
//            Optional<File> first = Arrays.stream(Objects.requireNonNull(directory.listFiles())).filter((file1 -> file1.getName().equals("level.dat"))).findFirst();
//            if (!first.isPresent()) break;
//            File level = first.get();
            File level = data.dataDir.toFile();
//            JsonObject levelJson = new String(FileUtils.readFileToByteArray(level), StandardCharsets.UTF_8);
            CompoundTag compoundTag;
            try {
                compoundTag = NbtIo.read(level);
            } catch (IOException e) {
//                throw new RuntimeException(e);
                LOGGER.fatal("{}@WorldReset.java:105", e.toString());
                break;
            }
            assert compoundTag != null;
            System.out.println(compoundTag.toText());
            System.out.println(compoundTag.getCompound("Data").getCompound("WorldGenSettings").toText());
        } while (false);
//        System.out.println(directory.toAbsolutePath());

    }

    protected static void unlock(WorldData data) throws IOException {
        // region experimental
        try {
            ;
//            data.threadedAnvilChunkStorageAccessor.untitled$save(true);
//            data.serverChunkManager.tick(() -> true);
//            for (int i = 0; i < 4; i++) {
//                data.serverChunkManagerAccessor.getChunkCache()[i] = null;
//            }
//            data.serverChunkManager.close();
//            data.serverChunkManager.
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
