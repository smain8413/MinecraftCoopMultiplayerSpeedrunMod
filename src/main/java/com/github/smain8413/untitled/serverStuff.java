package com.github.smain8413.untitled;

import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.storage.RegionBasedStorage;
import net.minecraft.world.gen.GeneratorOptions;
//import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.entity.boss.dragon.EnderDragonFight;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class serverStuff {

    public static void forceCloseRegionFiles(ServerWorld world) {
        try {
            ServerChunkManager chunkManager = world.getChunkManager();

            // Block and POI chunks
            Close(chunkManager.threadedAnvilChunkStorage);
            Close(chunkManager.getPointOfInterestStorage());

            // Entity chunks
//            Object entityManager = null;
//            for (Field f : ServerWorld.class.getDeclaredFields()) {
//                if (f.getType() == ServerEntityManager.class) {
//                    f.setAccessible(true);
//                    entityManager = f.get(world);
//                    break;
//                }
//            }
//            if (entityManager != null) Close(entityManager);

        } catch (Exception e) {}
    }

    static void Close(Object target) {
        CloseRecurse(target, new HashSet<>(), 0);
    }

    static void CloseRecurse(Object target, Set<Object> visited, int depth) {
        if (target == null || depth > 4 || !visited.add(target)) return;
        if (
                target instanceof net.minecraft.server.world.ServerWorld ||
//                target instanceof net.minecraft.server.MinecraftServer
                        target instanceof net.minecraft.server.PlayerManager ||
                        target instanceof net.minecraft.server.MinecraftServer ||
                        target instanceof net.minecraft.server.world.ServerChunkManager
        ) {
            return;
        }
        Class<?> current = target.getClass();
        while (current != null && current != Objects.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.getType().isPrimitive()) continue;
                String className = field.getType().getName();
                if (!className.startsWith("net.minecraft") && !className.startsWith("com.mojang")) continue;
                try {
                    field.setAccessible(true);
                    Object value = field.get(target);
                    if (value != null) {
                        CloseRecurse(target, visited, depth + 1);
                    }
                } catch (Exception ignored) {
                }
                current = current.getSuperclass();
            }
        }
    }
}
