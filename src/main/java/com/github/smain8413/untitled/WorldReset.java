package com.github.smain8413.untitled;

import com.github.smain8413.untitled.utils.QueuedAction;
import com.github.smain8413.untitled.utils.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.io.IOException;
import java.util.Random;

public class WorldReset {
    public static void Reset(MinecraftServer server) {
        deleteWorld(server.getWorld(ServerWorld.OVERWORLD));
    }

    protected static void deleteWorld(ServerWorld world) {
        WorldData data = new WorldData(world);
        unlock(data);
        data.generatorOptionsAccessor.setSeed(new Random().nextLong());
        queueRunAction(data, new QueuedAction(() -> {
            lock(data);
            returnPlayer(data);
        }, 10));
    }

    protected static void unlock(WorldData data) {
        try {
            data.session.deleteSessionLock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void lock(WorldData data) {

    }

    protected static void returnPlayer(WorldData data) {

    }

    protected static void queueRunAction(WorldData data, QueuedAction action) {
        data.serverWorldExtra.untitled$runNextTick(action);
    }

}
