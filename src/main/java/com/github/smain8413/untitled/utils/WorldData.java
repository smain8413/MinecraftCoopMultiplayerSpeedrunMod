package com.github.smain8413.untitled.utils;

import com.github.smain8413.untitled.ServerWorldExtras;
import com.github.smain8413.untitled.mixin.*;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
    public final Path rootDir;
    public final Path dataDir;
    public final File worldDir;
    public CompoundTag newLevelDat;
//    public boolean cleared = false;
//
//    private static final List<String> NOT_WORLD;
//
//    static {
//        NOT_WORLD = new ArrayList<>();
//        NOT_WORLD.add("NOT_WORLD");
//        NOT_WORLD.add("worldRegistryKey");
//        NOT_WORLD.add("cleared");
//        NOT_WORLD.add("session");
//        NOT_WORLD.add("sessionAccessor");
//        NOT_WORLD.add("generatorOptionsAccessor");
//        NOT_WORLD.add("Server");
//        NOT_WORLD.add("generatorOptionsAccessor");
//    }


    public WorldData(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
        this.serverWorldAccessor = (ServerWorldAccessor) serverWorld;
        this.server = serverWorld.getServer();
        this.serverAccessor = (ServerAccessor) server;
        this.session = serverAccessor.untitled$getSession();
        this.sessionAccessor = (SessionAccessor) session;
        this.serverChunkManager = serverWorldAccessor.untitled$getServerChunkManager();
        this.threadedAnvilChunkStorage = serverChunkManager.threadedAnvilChunkStorage;
        this.serverWorldExtra = (ServerWorldExtras) serverWorld;
        this.generatorOptionsAccessor = (GeneratorOptionsAccessor) serverWorld.getServer().getSaveProperties().getGeneratorOptions();
        this.worldRegistryKey = serverWorld.getRegistryKey();
        this.serverChunkManagerAccessor = (ServerChunkManagerAccessor) serverChunkManager;
        threadedAnvilChunkStorageAccessor = (ThreadedAnvilChunkStorageAccessor) threadedAnvilChunkStorage;
        rootDir = session.getDirectory(WorldSavePath.ROOT);
        dataDir = session.getDirectory(WorldSavePath.LEVEL_DAT);
        this.worldDir = session.getWorldDirectory(this.worldRegistryKey);
        try {
            this.newLevelDat = newLevelData();
        } catch (Exception e) {
            LOGGER.fatal(e);
        }
    }

    private CompoundTag newLevelData() throws IOException {
//        System.out.println("newLevelData (WorldData.java)");
////        SessionLock.create(rootDir);
//        FileChannel channel = FileChannel.open(dataDir, StandardOpenOption.READ, StandardOpenOption.SYNC);
//        channel.position(0);
//        InputStream inputStream = Channels.newInputStream(channel);
//        inputStream.skip(5);
//        ByteBuffer buf = ByteBuffer.allocateDirect(2048);
//        channel.read(buf);
//        buf = buf.asReadOnlyBuffer();
//        buf.rewind();
//        System.out.println(Arrays.toString(buf.array()));
//        NbtIo.read(buf.);
        CompoundTag data = new CompoundTag();
//        data.put("Horse", (Tag) BlockTags.ACACIA_LOGS);
//        try {
//            data = NbtIo.readCompressed(dataDir);
        GZIPInputStream gzip = new GZIPInputStream(Files.newInputStream(dataDir));
        data = NbtIo.read(new DataInputStream(gzip));
//        } finally {
//            System.out.println(data.toText());
//        }
//        if (data == null) throw new NullPointerException();
//        System.out.println(data.toText());
//        Files.newOutputStream(dataDir);
        return dataWithNewSeed(data, new Random().nextLong());
    }
    private CompoundTag dataWithNewSeed(CompoundTag data, Long seed) {
        LongTag seedTag = LongTag.of(seed);
        data.getCompound("Data").getCompound("WorldGenSettings").remove("seed");
        data.getCompound("Data").getCompound("WorldGenSettings").put("seed", seedTag);
        for (ServerWorld serverWorld : server.getWorlds() ) {
            String worldID = serverWorld.getRegistryKey().getValue().toString();
            data.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions").getCompound(worldID).getCompound("generator").remove("seed");
            data.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions").getCompound(worldID).getCompound("generator").put("seed", seedTag);
            data.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions").getCompound(worldID).getCompound("generator").getCompound("biome_source").remove("seed");
            data.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions").getCompound(worldID).getCompound("generator").getCompound("biome_source").put("seed", seedTag);
        }
        data.getCompound("Data").remove("Time");
        data.getCompound("Data").remove("DayTime");
        data.getCompound("Data").put("Time", LongTag.of(0));
        data.getCompound("Data").put("DayTime", LongTag.of(0));
        // try this
        data.getCompound("Data").remove("Player");
        return data;
    }
//
//    public void clearWorldData() {
//        Field[] declaredFields = this.getClass().getDeclaredFields();
//        for (Field field : declaredFields) {
//            if (NOT_WORLD.parallelStream().anyMatch(string -> string.equals(field.getName()))) continue;
//            try {
//                field.setAccessible(true);
//                field.set(this, null);
//            } catch (IllegalAccessException e) {
//                LOGGER.fatal(e.toString());
//            }
//        }
//        cleared = true;
//    }

    public boolean isWorld(@Nullable RegistryKey<World> world) {
        if (world == null) {
            LOGGER.fatal(new NullPointerException("Comparison world is null (WorldData.java Line 57)").toString());
            return false;
        }
        return world.equals(this.serverWorld.getRegistryKey());
    }
}