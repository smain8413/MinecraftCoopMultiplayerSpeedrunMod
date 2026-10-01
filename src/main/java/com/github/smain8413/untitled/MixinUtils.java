package com.github.smain8413.untitled;

import com.github.smain8413.untitled.mixin.ServerAccessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.ZombieSiegeManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.*;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.SessionLock;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.smain8413.untitled.Untitled.*;

public class MixinUtils {

    public static void resetWorld(MinecraftServer server, long seed) {
        LevelStorage.Session session;
        try {
            session = getPrivate(MinecraftServer.class.getDeclaredField("session"), server);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error(e.toString());
            return;
//            throw new RuntimeException(e);
        }
        Iterable<ServerWorld> worlds = server.getWorlds();
        //region unloadWorlds
        List<ServerPlayerEntity> players = worlds.iterator().next().getPlayers();
        ServerWorld tempWorld;
        try {
            tempWorld = temporaryWorld(server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error(e.toString());
            return;
//            throw new RuntimeException(e);
        }
        ((ServerAccessor)server).getWorlds().put(TEMP_WORLD, tempWorld);
//        worlds.forEach(world -> world.savingDisabled = true);
//        players.forEach(player -> player.teleport(tempWorld, 0F, tempWorld.getTopY(Heightmap.Type.WORLD_SURFACE, 0, 0), 0F, 0f, 0f));
//        players.forEach(player -> player.teleport(tempWorld, 0f, 255f, 0f, 0f, 0f));
        try {
            for (ServerPlayerEntity player : players) {
                player.teleport(tempWorld, 0f, 255f, 0f, 0f, 0f);
            }
        } catch (Exception e) {
            LOGGER.fatal(e.toString());
//            throw new RuntimeException(e);
        }

        //endregion unloadWorlds


        List<File> worldDirs = Arrays.stream(Iterables.toArray(worlds, ServerWorld.class)).parallel().map(world -> session.getWorldDirectory(world.getRegistryKey())).collect(Collectors.toList());
        worldDirs.remove(session.getWorldDirectory(tempWorld.getRegistryKey()));
        try {
            session.deleteSessionLock();
        } catch (IOException iOException2) {
            LOGGER.error("Failed to unlock level {}", session.getDirectoryName(), iOException2);
        }

        //noinspection ResultOfMethodCallIgnored
        worldDirs.forEach(File::delete);

        try {
            setPrivate(GeneratorOptions.class.getDeclaredField("seed"), seed, server.getSaveProperties().getGeneratorOptions());
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        worlds.forEach(world -> world.savingDisabled = false);
        try {
            setPrivate(session.getClass().getDeclaredField("lock"), SessionLock.create(session.getDirectory(WorldSavePath.ROOT)), session);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        players.forEach(ServerPlayerEntity::kill);
//        worlds.forEach(world -> world.save());
    }



    public static ServerWorld temporaryWorld(MinecraftServer server) throws NoSuchFieldException, IllegalAccessException {
        LevelStorage.Session session = getPrivate(MinecraftServer.class.getDeclaredField("session"), server);
        List<Spawner> list = ImmutableList.of(new PhantomSpawner(), new WanderingTraderManager(server.getSaveProperties().getMainWorldProperties()));

        // fix registrykey / dimensionType
        ServerWorld world = new ServerWorld(server, Util.getServerWorkerExecutor(), session,
                server.getSaveProperties().getMainWorldProperties(), TEMP_WORLD, TEMP_WORLD_TYPE,
                DimensionType.getOverworldCavesDimensionType(), dummyGenerationProgressListener,
                Objects.requireNonNull(server.getWorld(World.END)).getChunkManager().getChunkGenerator(), true, BiomeAccess.hashSeed(1L), list, false);
//        world.savingDisabled = true;
        server.getWorlds();
        return world;
    }

    public static <Any> Any getPrivate(Field field, Object instance) throws IllegalAccessException {
        field.setAccessible(true);
        //noinspection unchecked
        return (Any) field.get(instance);
    }

    public static void setPrivate(Field field, Object value, Object instance) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
//        field.setAccessible(false);
    }

    static WorldGenerationProgressListener dummyGenerationProgressListener = new WorldGenerationProgressListener() {
        @Override
        public void start(ChunkPos spawnPos) {

        }

        @Override
        public void setChunkStatus(ChunkPos pos, @Nullable ChunkStatus status) {

        }

        @Override
        public void stop() {

        }
    };
}
