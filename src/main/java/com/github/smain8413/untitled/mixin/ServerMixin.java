package com.github.smain8413.untitled.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.SessionLock;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;


@Mixin(MinecraftServer.class)
public abstract class ServerMixin extends ReentrantThreadExecutor<ServerTask> implements SnooperListener, CommandOutput, AutoCloseable {

    @Unique
    private int Triggers = 0;

    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;


    @Final
    @Shadow
    protected LevelStorage.Session session;


    @Shadow
    @Final
    private static Logger LOGGER;

//    public static Map<RegistryKey<World>, ServerWorld> getWorlds() => worlds;
    @Unique
    private static Object instance;

    public ServerMixin(String string) {
        super(string);
    }
//    @Unique
//    public static Object getServerMixinInstance() {return instance;}

//    @Inject(method = "save", at = @At(value = "TAIL"))
//    private void onSave(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir){
//        instance = this;
//        if (Triggers > 1) return;
//        Triggers++;
//        System.out.printf("Triggers: %d%n", Triggers);
//        MinecraftServer ts = (MinecraftServer) (Object) this;
//        System.out.println("world saved");
//        ServerWorld endWorld = worlds.get(World.END);
//        ServerWorld overWorld = worlds.get(World.OVERWORLD);
//        if (Triggers == 1) {
//            ServerPlayerEntity player = overWorld.getRandomAlivePlayer();
//            if (player != null) {
//                player.teleport(endWorld, 0d, 0d, 0d, 0f, 0f);
//                player.kill();
//            }
//            return;
//        }
//
//
//        try {
//            session.deleteSessionLock();
//        } catch (IOException iOException2) {
//            LOGGER.error("Failed to unlock level {}", session.getDirectoryName(), iOException2);
//        }
//
//        File worldDir = session.getWorldDirectory(World.OVERWORLD);
//        //noinspection ResultOfMethodCallIgnored
//        worldDir.delete();
//
//        try {
//            setPrivate(GeneratorOptions.class.getDeclaredField("seed"), new Random().nextLong() /*Calvin's seed filtering goes here*/, ts.getSaveProperties().getGeneratorOptions());
//        } catch (Exception e) {
//            System.out.println("E1");
//            LOGGER.error(e.toString());
//        }
//        overWorld.getSeed();
//        try {
//            setPrivate(session.getClass().getDeclaredField("lock"), SessionLock.create(session.getDirectory(WorldSavePath.ROOT)), session);
//        } catch (Exception e) {
//            System.out.println("E2");
//            LOGGER.error(e.toString());
//        }
//    }
//
//    @Inject(method = "close", at = @At("HEAD"))
//    private void onClose(CallbackInfo ci) {
//        instance = null;
//        Triggers = 0;
//    }
//
//    @Unique
//    private static void setPrivate(Field field, Object value, Object instance) throws IllegalAccessException {
//        field.setAccessible(true);
//        field.set(instance, value);
//    }

}
