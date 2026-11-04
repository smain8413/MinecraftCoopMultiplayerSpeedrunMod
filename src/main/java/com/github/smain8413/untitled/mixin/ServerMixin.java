package com.github.smain8413.untitled.mixin;

import com.github.smain8413.untitled.Untitled;
import com.github.smain8413.untitled.WorldReset;
import com.github.smain8413.untitled.utils.WorldData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


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

    @Shadow
    protected abstract void createWorlds(WorldGenerationProgressListener worldGenerationProgressListener);

    @Shadow
    @Final
    private WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory;
    //    public static Map<RegistryKey<World>, ServerWorld> getWorlds() => worlds;
    @Unique
    private static Object instance;

    public ServerMixin(String string) {
        super(string);
    }

    @Inject(method = "startServer", at = @At("TAIL"))
    private static <S extends MinecraftServer> void onStartServer(Function<Thread, S> serverFactory, CallbackInfoReturnable<S> cir) {
        Untitled.SERVER = cir.getReturnValue();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        Untitled.SERVER = null;
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void onSave(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> cir) {
//        boolean fileEdit = false;
//        WatchKey key = null;
//        try {
//            while ((key = Untitled.WATCHER.poll(10, TimeUnit.MICROSECONDS)) != null) {
////                System.out.println(Arrays.toString(getThread().getStackTrace()));
//
////                System.out.println(task.getClass().getClass());
////                System.out.println(task + ": ");
//
////                if(task instanceof Lambda)
//                for (WatchEvent<?> event : key.pollEvents()) {
//                    fileEdit = fileEdit || event.context().toString().equals("level.dat");
//                    if (!event.context().toString().equals("level.dat")) continue;
//                    //process
//                    System.out.println(
//                            "Event kind:" + event.kind()
//                                    + ". File affected: " + event.context() + ".");
////                    if (task instanceof CompletableFuture) {
//////                        ((CompletableFuture) task)
////                        CompletableFuture.UniCompose.class
//
////                    }
//
//                }
//                key.reset();
//            }
//        } catch (Exception e) {
//            LOGGER.fatal("very bad (ThreadExecutorMixin 1)");
//        }
//        if (fileEdit) {
//            WorldReset.findAndEditLevelDat(new WorldData(((MinecraftServer)((Object)this)).getOverworld()));
////            createWorlds(worldGenerationProgressListenerFactory.create(100));
//        }
//        while (true) {
////            key = null;
//            try {
//                if ((key = Untitled.WATCHER.poll(10, TimeUnit.MICROSECONDS)) == null) break;
//            } catch (InterruptedException e) {
////                throw new RuntimeException(e);
//                LOGGER.fatal(e.fillInStackTrace().toString());
//            }
//            assert key != null;
//            key.reset();
//        }
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
