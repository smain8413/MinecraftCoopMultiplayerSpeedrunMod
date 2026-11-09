package com.github.smain8413.untitled;

//import com.github.smain8413.untitled.utils.AdvancementTrackerExtras;
import com.github.smain8413.untitled.utils.ProgressData;
import com.github.smain8413.untitled.utils.QueuedAction;
import com.github.smain8413.untitled.utils.ServerExtras;
import com.github.smain8413.untitled.utils.WorldData;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
import com.mojang.serialization.Lifecycle;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

import static com.github.smain8413.untitled.Untitled.LOGGER;

public class WorldReset {
    public static void Reset(MinecraftServer server) {
        deleteWorld(server.getWorld(ServerWorld.OVERWORLD));
    }

    protected static void deleteWorld(ServerWorld world) {
        long seed = new Random().nextLong();
        WorldData data = new WorldData(world);
        evacuatePlayers(new WorldData(data.server.getOverworld()));
        GeneratorOptions generatorOptions = data.server.getSaveProperties().getGeneratorOptions();
        SimpleRegistry<DimensionOptions> dimensionMap = generatorOptions.getDimensionMap(); // Generator in each dim
        SimpleRegistry<DimensionOptions> newDimensionMap = new SimpleRegistry<>(Registry.DIMENSION_OPTIONS, Lifecycle.stable() /*Won't call the world experimental on load?*/);
        for (DimensionOptions dimensionOptions : dimensionMap) {
            DimensionOptions newOptions = new DimensionOptions(dimensionOptions.getDimensionTypeSupplier(), dimensionOptions.getChunkGenerator().withSeed(seed)); // the same but with new seeds
            //noinspection OptionalGetWithoutIsPresent
            newDimensionMap.add(dimensionMap.getKey(dimensionOptions).get(), newOptions);
        }
        data.server.getSaveProperties().getMainWorldProperties().setInitialized(false); // new spawn I think
//        data.generatorOptionsAccessor.setSeed(seed);
        data.generatorOptionsAccessor.untitled$setOptions(newDimensionMap);
        deleteWorldDirectory(data);

        // queue re-lock
        queueRunAction(data, new QueuedAction(() -> {
            data.serverAccessor.untitled$createWorlds(data.serverAccessor.untitled$getWorldGenerationProgressListenerFactory().create(11));
            killEveryone(data);
        }, 0));

        queueRunAction(new WorldData(data.server.getOverworld()), new QueuedAction(() -> {
            for (int i = 0; i < data.serverWorld.getPlayers().size(); i++) {
                data.server.getPlayerManager().respawnPlayer(data.serverWorld.getPlayers().get(i), false);
            }
        }, 1));
    }

    protected static void evacuatePlayers(WorldData data) { // doImmediateRespawn
//        ServerWorld destination = Objects.requireNonNull(data.server.getWorld(data.isWorld(World.OVERWORLD) ? World.END : World.OVERWORLD));
        ServerWorld destination = data.serverWorld;
        for (int i = 0; i < data.serverWorld.getPlayers().size(); i++) {
            ServerPlayerEntity player = data.serverWorld.getPlayers().get(i);
            BlockPos destinationSpawn = destination.getSpawnPos();
            if (destination.getRegistryKey().equals(ServerWorld.END)) destinationSpawn.add(0, 100000, 0);
            player.teleport(destination, destinationSpawn.getX(), destinationSpawn.getY() + 1, destinationSpawn.getZ(), 0, 0);
        }
//        new WorldData(destination);
    }

    // send players back to overworld through death
    protected static void killEveryone(WorldData data) {
        List<ServerPlayerEntity> players = data.serverWorld.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            players.get(i).inventory.clear();
            players.get(i).getEnderChestInventory().clear();
            Collection<Advancement> collection = data.server.getAdvancementLoader().getAdvancements();
            for (Advancement advancement: collection) {
                Iterable<String> criteria = players.get(i).getAdvancementTracker().getProgress(advancement).getObtainedCriteria();
                for (String criterion: criteria) {
                    players.get(i).getAdvancementTracker().revokeCriterion(advancement, criterion);
                }
            }
            players.get(i).kill();
        }
    }

    protected static void queueRunAction(WorldData data, QueuedAction action) {
        data.serverWorldExtra.untitled$runNextTick(action);
    }

    protected static void deleteWorldDirectory(WorldData data) {

        try {
            recursiveCleanDirectory(data.worldDir, List.of("level.dat", "raids.dat", "raids_end.dat", "icon.png", "stats", "datapacks", "session.lock", ProgressData.getProgressDataPath(data.server).toFile().getName()));
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
